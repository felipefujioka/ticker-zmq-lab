package com.topster.zmqproxy;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class App {

  private enum Side {
    NONE,
    MESSAGE,
    SUBSCRIPTION
  };

  public static void main( String[] args) {
    Context context = ZMQ.context(1);

    //  Socket facing clients
    Socket cloudSub = context.socket(ZMQ.XSUB);
    cloudSub.bind("tcp://*:" + System.getenv("SUB_PORT"));

    //  Socket facing services
    Socket cloudPub = context.socket(ZMQ.XPUB);
    cloudPub.setXpubVerbose(true);
    cloudPub.bind("tcp://*:" + System.getenv("PUB_PORT"));

    Socket localPub = context.socket(ZMQ.PUB);
    localPub.bind("tcp://*:" + System.getenv("LOCAL_PUB_PORT"));

    Socket localSub = context.socket(ZMQ.SUB);
    localSub.subscribe("".getBytes());
    localSub.connect("tcp://192.168.99.100:" + System.getenv("LOCAL_SUB_PORT"));

    Poller items = new Poller (3);
    items.register(cloudSub, Poller.POLLIN);
    items.register(cloudPub, Poller.POLLIN);
    items.register(localSub, Poller.POLLIN);

    //  Start the proxy
    // ZMQ.proxy (sub, pub, backup);

    boolean more = false;
    byte[] message;

    while (!Thread.currentThread().isInterrupted()) {
      //  poll and memorize multipart detection
      Side side = Side.NONE;
      items.poll();

      if (items.pollin(0)) {
        localPub.send("MESSAGE".getBytes(), ZMQ.SNDMORE);
        while (true) {
          // receive message
          message = cloudSub.recv(0);
          more = cloudSub.hasReceiveMore();
          // System.out.println("received from publisher: " + new String(message));
          // Broker it
          cloudPub.send(message, more ? ZMQ.SNDMORE : 0);
          localPub.send(message, more ? ZMQ.SNDMORE : 0);
          if (!more) {
            break;
          }
        }
      }

      if (items.pollin(1)) {
        localPub.send("SUBSCRIPTION".getBytes(), ZMQ.SNDMORE);
        while (true) {
          // receive message
          message = cloudPub.recv(0);
          more = cloudPub.hasReceiveMore();
          // Broker it
          cloudSub.send(message,  more ? ZMQ.SNDMORE : 0);
          localPub.send(message,  more ? ZMQ.SNDMORE : 0);
          if (!more) {
            break;
          }
        }
      }

      if (items.pollin(2)) {
        byte[] receivedSide = localSub.recv(0);
        if (new String(receivedSide).equals("SUBSCRIPTION")) {
          side = Side.SUBSCRIPTION;
        } else if (new String(receivedSide).equals("MESSAGE")) {
          side = Side.MESSAGE;
        }

        while (true) {
          // receive message
          message = localSub.recv(0);
          more = localSub.hasReceiveMore();
          // Broker it
          if (side == Side.SUBSCRIPTION) {
            cloudSub.send(message,  more ? ZMQ.SNDMORE : 0);
          } else if (side == Side.MESSAGE) {
            cloudPub.send(message,  more ? ZMQ.SNDMORE : 0);
          }

          if (!more) {
            break;
          }
        }
      }
    }

    //  We never get here but clean up anyhow
    cloudSub.close();
    cloudPub.close();
    localSub.close();
    localPub.close();

    context.term();
  }
}

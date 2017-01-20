package com.topster.zmqproxy;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class App {

  private enum Destination {
    SUBSCRIBERS,
    PUBLISHERS
  };

  public static void main(String[] args) {
    Context context = ZMQ.context(1);

    //  Socket facing publishers
    Socket cloudSub = context.socket(ZMQ.XSUB);
    cloudSub.bind("tcp://*:" + System.getenv("SUB_PORT"));

    //  Socket facing subscribers
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

    boolean more = false;
    byte[] message;

    while (!Thread.currentThread().isInterrupted()) {
      items.poll();

      if (items.pollin(0)) {
        localPub.send(Destination.SUBSCRIBERS.name().getBytes(), ZMQ.SNDMORE);
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
        localPub.send(Destination.PUBLISHERS.name().getBytes(), ZMQ.SNDMORE);
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
        String destinationStr = new String(localSub.recv(0));
        Destination destination = Destination.valueOf(destinationStr);

        while (true) {
          // receive message
          message = localSub.recv(0);
          more = localSub.hasReceiveMore();
          // Broker it
          if (destination == Destination.SUBSCRIBERS) {
            cloudPub.send(message,  more ? ZMQ.SNDMORE : 0);
          } else if (destination == Destination.PUBLISHERS) {
            cloudSub.send(message,  more ? ZMQ.SNDMORE : 0);
          }
          // TODO: else raise Exception ?

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

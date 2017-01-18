package com.topster.zmqproxy;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

public class App {
  public static void main( String[] args) {
        Context context = ZMQ.context(1);

        //  Socket facing clients
        Socket sub = context.socket(ZMQ.XSUB);
        sub.bind("tcp://*:" + System.getenv("SUB_PORT"));

        //  Socket facing services
        Socket pub = context.socket(ZMQ.XPUB);
        pub.setXpubVerbose(true);
        pub.bind("tcp://*:" + System.getenv("PUB_PORT"));

        Socket backupPub = context.socket(ZMQ.PUB);
        backupPub.connect("tcp://192.168.99.100:" + System.getenv("NEXT_PORT_PUB"));

        Socket backupSub = context.socket(ZMQ.PUB);
        backupSub.connect("tcp://192.168.99.100:" + System.getenv("NEXT_PORT_SUB"));

        Poller items = new Poller (2);
        items.register(sub, Poller.POLLIN);
        items.register(pub, Poller.POLLIN);

        //  Start the proxy
        // ZMQ.proxy (sub, pub, backup);

        boolean more = false;
        byte[] message;

        while (!Thread.currentThread().isInterrupted()) {            
            //  poll and memorize multipart detection
            items.poll();

            if (items.pollin(0)) {
                while (true) {
                    // receive message
                    message = sub.recv(0);
                    more = sub.hasReceiveMore();
                    System.out.println("incoming: " + new String(message));
                    // Broker it
                    pub.send(message, more ? ZMQ.SNDMORE : 0);
                    backupPub.send(message, more ? ZMQ.SNDMORE : 0);
                    if(!more){
                        break;
                    }
                }
            }
            if (items.pollin(1)) {
                while (true) {
                    // receive message
                    message = pub.recv(0);
                    more = pub.hasReceiveMore();
                    System.out.println("outcoming: " + new String(message));
                    // Broker it
                    sub.send(message,  more ? ZMQ.SNDMORE : 0);
                    backupSub.send(message, more ? ZMQ.SNDMORE : 0);
                    if(!more){
                        break;
                    }
                }
            }
        }

        //  We never get here but clean up anyhow
        sub.close();
        pub.close();
        context.term();
  }
}

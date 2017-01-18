package com.topster.zmqproxy;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class App {
  public static void main( String[] args) {
        Context context = ZMQ.context(1);

        //  Socket facing clients
        Socket sub = context.socket(ZMQ.XSUB);
        sub.bind("tcp://*:3001");

        //  Socket facing services
        Socket pub = context.socket(ZMQ.XPUB);
        pub.bind("tcp://*:3002");

        //  Start the proxy
        ZMQ.proxy (sub, pub, null);

        //  We never get here but clean up anyhow
        sub.close();
        pub.close();
        context.term();
  }
}

package com.topster.zmqproxy;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Poller;
import org.zeromq.ZMQ.Socket;

import java.util.ArrayList;

public class App {

  private static final String SUB_ADDR="tcp://127.0.0.1:9999";

  private final static int IDS = 1;
  private final static int QNT = 5;

  public static void main(String[] args) {
    Context context = ZMQ.context(1);

    ArrayList<Thread> ths = new ArrayList<Thread>();

    for(int i = 1; i <= IDS; i++) {
      for(int j = 1; j <= QNT; j++) {
        Consumer c = new Consumer(i, j, context);

        Thread t = new Thread(c);

        ths.add(t);

        t.start();

      }
    }

    for (int i = 0; i < ths.size(); i++) {
      try {
        ths.get(i).join();
      }catch(InterruptedException e) {

      }
    }

    //  We never get here but clean up anyhow
    context.term();
  }

  public static class Consumer implements Runnable {

    private int id;

    private int order;

    private Context context;

    public Consumer(int id, int order, Context context) {
      this.id = id;
      this.order = order;
      this.context = context;
    }

    @Override
    public void run() {

      //  Socket facing publishers
      Socket subSocket = context.socket(ZMQ.SUB);
      subSocket.connect(SUB_ADDR);
//      if (this.id < 10) {
//        subSocket.subscribe(("TICK.T0" + this.id).getBytes());
//      }else {
//        subSocket.subscribe(("TICK.T" + this.id).getBytes());
//      }

      subSocket.subscribe("".getBytes());

      boolean more = false;
      String message;
      int inc = 0;
      long initialTime = System.currentTimeMillis();

      while (!Thread.currentThread().isInterrupted()) {
        while (true) {
          // receive message
          message = subSocket.recvStr(0);
          more = subSocket.hasReceiveMore();

          if (!more) {
//            inc++;

//            if(inc >= 10000) {
//              inc = 0;
//              System.out.println("time to subscriber " + this.id + "-" + this.order + " receive 10000 msgs: " + (System.currentTimeMillis() - initialTime));
//              initialTime = System.currentTimeMillis();
//            }
            break;
          }
        }
      }
      subSocket.close();
    }
  }

}

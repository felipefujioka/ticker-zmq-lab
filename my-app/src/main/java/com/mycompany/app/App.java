package com.mycompany.app;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

public class App {
  public static void main( String[] args) {
    ZMQ.Context context = ZMQ.context(1);

    System.out.println("Collecting updates from weather server");
    ZMQ.Socket subscriber = context.socket(ZMQ.SUB);
    subscriber.connect("tcp://192.168.99.100:3002");

    subscriber.subscribe("".getBytes(ZMQ.CHARSET));
    while (!Thread.currentThread().isInterrupted()) {
      String topic = subscriber.recvStr();
      String timestamp = subscriber.recvStr();
      String message = subscriber.recvStr();
      // System.out.println(topic + ": " + timestamp + ": " + message);
    }

    subscriber.close();
    context.term();
  }
}

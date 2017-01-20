package com.mycompany.javaticker;

import java.util.Date;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;

public class App 
{

  private static class Message {
    String price;
    String seq;
  }

    public static void main( String[] args )
    {
      Context context = ZMQ.context(1);

      //  Socket facing publishers
      Socket pub = context.socket(ZMQ.PUB);
      pub.connect("tcp://192.168.99.100:" + System.getenv("PUB_PORT"));

      while(true) {
        System.out.println("sending tick to " + System.getenv("SECURITY_SYMBOL"));
        pub.send(System.getenv("SECURITY_SYMBOL").getBytes(), ZMQ.SNDMORE);
        pub.send(new Date().toString().getBytes(), ZMQ.SNDMORE);
        Message message = new Message();
        message.price = "123";
        message.seq= "1";
        pub.send(new Gson().toJson(message).getBytes(), 0);
        try {
          Thread.sleep(Integer.parseInt(System.getenv("TICK_PERIOD")));
        }catch(java.lang.InterruptedException e) {

        }
      }

      pub.close();

      context.term();
    }
}

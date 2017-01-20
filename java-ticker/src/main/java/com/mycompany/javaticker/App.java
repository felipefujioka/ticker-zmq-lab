package com.mycompany.javaticker;

import java.util.Date;

import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import com.google.gson.Gson;

public class App 
{

  static Integer seq = 0;

  private static class Message {
    String price;
    String seq;

    public Message(Double price, Integer seq) {
      this.price = price.toString();
      this.seq = seq.toString();

    }
  }

  public static Double addRandomVariationTo(String price) {
    Double fPrice = Double.parseDouble(price);
    Double signal = Math.random() > 0.5 ? -1.0 : 1.0;
    Double variation = Math.random();
    return fPrice + signal*variation;
  }

  public static void main( String[] args )
    {
      Context context = ZMQ.context(1);

      Socket pub = context.socket(ZMQ.PUB);
      System.out.println("tcp://192.168.99.100:" + System.getenv("PUB_PORT"));
      pub.connect("tcp://192.168.99.100:" + System.getenv("PUB_PORT"));

      while(true) {
        System.out.println("sending tick to " + System.getenv("SECURITY_SYMBOL"));
        pub.send(("TICK." + System.getenv("SECURITY_SYMBOL")).getBytes(), ZMQ.SNDMORE);
        pub.send(("" + new Date().getTime()).getBytes(), ZMQ.SNDMORE);
        Message message = new Message(App.addRandomVariationTo(System.getenv("BASE_PRICE")), App.seq++);
        String msgStr = new Gson().toJson(message);
        System.out.println("sending message: " + msgStr);
        pub.send(msgStr.getBytes(), 0);
        try {
          Thread.sleep(Integer.parseInt(System.getenv("TICK_PERIOD")));
        }catch(java.lang.InterruptedException e) {

        }
      }
    }
}

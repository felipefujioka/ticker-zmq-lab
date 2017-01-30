package com.mycompany.prodCon;

import org.fabric3.api.annotation.model.Component;
import org.fabric3.api.annotation.Producer;

import org.fabric3.api.implementation.timer.annotation.Timer;
import org.fabric3.api.implementation.timer.model.TimerType;

import com.google.gson.Gson;

import com.shared.Tick;

@Timer(type = TimerType.RECURRING)
@Component(composite= "{urn:mycompany.com}ChannelComposite")
public class TickProducer implements Runnable {

  @Producer(target="TickChannel")
  private TickChannel tickChannel;
 
  public void run() {

    System.out.println("ticking for " + System.getenv("SECURITY_SYMBOL"));

    Tick tick = new Tick();
    tick.price = Math.random();
    System.out.println("new tick: " + tick);

    tickChannel.publish(new byte[][] {
      ("TICK."+System.getenv("SECURITY_SYMBOL")).getBytes(),
      new Long(System.currentTimeMillis()).toString().getBytes(),
      new Gson().toJson(tick).getBytes()
    });

  }

  public long nextInterval() {
    return 1000;
  }

}

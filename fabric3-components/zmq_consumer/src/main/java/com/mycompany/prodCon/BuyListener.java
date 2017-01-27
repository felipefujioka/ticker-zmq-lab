package com.mycompany.prodCon;

import org.fabric3.api.annotation.model.Component;
import org.fabric3.api.annotation.Consumer;

import org.fabric3.api.implementation.timer.annotation.Timer;
import org.fabric3.api.implementation.timer.model.TimerType;

import com.google.gson.Gson;

import com.shared.Tick;

@Component(composite= "{urn:mycompany.com}ChannelComposite")
public class BuyListener {

  @Consumer(source="BuyChannel")
  public void onEvent(byte[][] message) {

    System.out.println(new String(message[0]));
    System.out.println(new String(message[1]));
    System.out.println(new String(message[2]));

  }

}

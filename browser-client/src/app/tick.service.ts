import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';

import * as IO from 'socket.io-client';

@Injectable()
export class TickService {

  tickStream: Subject<any>;
  petr4Stream: Subject<any>;
  vale5Stream: Subject<any>;
  connected: boolean = false;

  constructor() {
    this.tickStream = new Subject();
    this.petr4Stream = new Subject();
    this.vale5Stream = new Subject();
    this.create();
  }

  create() {
    var genericSocket = IO('ws://192.168.99.100:3003');
    var petr4Socket = IO('ws://192.168.99.100:3004');
    var vale5Socket = IO('ws://192.168.99.100:3005');

    genericSocket.on('connect', this.onConnect.bind(this));

    genericSocket.on('message', this.onMessageGeneric.bind(this));

    petr4Socket.on('message', this.onMessagePetr4.bind(this));

    vale5Socket.on('message', this.onMessageVale5.bind(this));

    genericSocket.on('disconnect', this.onDisconnect.bind(this));
  }

  onConnect() {
    this.connected = true;
  }

  onMessagePetr4(data) {
    console.log(JSON.parse(data));
    let parsedData = JSON.parse(data);
    let tick = {
      stock: parsedData.topic.replace("TICK.", ""),
      price: parsedData.message.price,
      timestamp: parsedData.timestamp
    }
    this.petr4Stream.next(tick);
  }

  onMessageVale5(data) {
    console.log(JSON.parse(data));
    let parsedData = JSON.parse(data);
    let tick = {
      stock: parsedData.topic.replace("TICK.", ""),
      price: parsedData.message.price,
      timestamp: parsedData.timestamp
    }
    this.vale5Stream.next(tick);
  }

  onMessageGeneric(data) {
    console.log(JSON.parse(data));
    let parsedData = JSON.parse(data);
    let tick = {
      stock: parsedData.topic.replace("TICK.", ""),
      price: parsedData.message.price,
      timestamp: parsedData.timestamp
    }
    this.tickStream.next(tick);
  }

  onDisconnect() {
    this.connected = false;
  }

}

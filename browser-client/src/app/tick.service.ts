import { Injectable } from '@angular/core';

import { Subject } from 'rxjs';

import * as IO from 'socket.io-client';

@Injectable()
export class TickService {

  server: any;
  tickStream: Subject<any>;
  connected: boolean = false;

  constructor() {
    this.tickStream = new Subject();
    this.server = this.create();
  }

  create() {
    var self = this;
    var socket = IO('ws://192.168.99.100:3003');

    socket.on('connect', this.onConnect.bind(this));

    socket.on('message', this.onMessage.bind(this));

    socket.on('disconnect', this.onDisconnect.bind(this));

    return socket;
  }

  onConnect() {
    this.connected = true;
  }

  onMessage(data) {
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

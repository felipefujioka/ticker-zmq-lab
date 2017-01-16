import { Injectable } from '@angular/core';

import { BehaviorSubject } from 'rxjs';

import * as IO from 'socket.io-client';

@Injectable()
export class TickService {

  server: any;
  tickStream: BehaviorSubject<any>;

  constructor() {
    this.tickStream = new BehaviorSubject({});
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
    this.tickStream.next("connected");
  }

  onMessage(tick) {
    console.log(JSON.parse(tick));
    this.tickStream.next(JSON.parse(tick));
  }

  onDisconnect() {
    this.tickStream.next("disconnected");
  }

}

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
    var socket = IO('ws://192.168.99.100:3002');
    
    socket.on('connect', this.onConnect.bind(this));

    socket.on('message', this.onMessage.bind(this));

    socket.on('disconnect', this.onDisconnect.bind(this));

    return socket;
  }

  onConnect() {
    this.tickStream.next("connected");
  }

  onMessage(data) {
    this.tickStream.next(data);
  }

  onDisconnect() {
    this.tickStream.next("disconnected");
  }

}

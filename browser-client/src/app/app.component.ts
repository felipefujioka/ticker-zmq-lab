import { Component, OnInit } from '@angular/core';

import { TickService } from './tick.service';

import * as _ from 'lodash';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ TickService ]
})
export class AppComponent implements OnInit{
  
  lastTick: any;
  stocks: {[id: string]: any} = {};
  keys: string[] = [];

  constructor(private tickService: TickService) { }

  ngOnInit() {
    this.tickService.tickStream.subscribe(tick => {
      this.lastTick = tick;
      this.stocks[tick.stock] = tick;
      this.keys = Object.keys(this.stocks);
      console.log(this.keys);
      console.log(this.stocks);
    });
  }


}

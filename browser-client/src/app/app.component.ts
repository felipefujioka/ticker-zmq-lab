import { Component, OnInit } from '@angular/core';

import { TickService } from './tick.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  providers: [ TickService ]
})
export class AppComponent implements OnInit{
  
  lastTick: any;

  constructor(private tickService: TickService) { }

  ngOnInit() {
    this.tickService.tickStream.subscribe(tick => {
      this.lastTick = tick;
    });
  }


}

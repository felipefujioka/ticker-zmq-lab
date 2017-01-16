// ticker
var zmq = require('zeromq')
  , pub = zmq.socket('pub');

pub.bindSync('tcp://0.0.0.0:3000');
console.log('Producer bound to port 3000');

setInterval(function() {
  var tick = {
    "price": 21.12
  }
  console.log('Sending tick', JSON.stringify(tick));
  pub.send(['PETR4', JSON.stringify(tick)]);
}, 1000);

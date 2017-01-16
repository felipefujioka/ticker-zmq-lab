// ticker
var zmq = require('zeromq')
  , pub = zmq.socket('pub');

pub.bindSync('tcp://0.0.0.0:3000');
console.log('Producer bound to port 3000');

function addRandomVariation(price) {
  var delta = Math.round(Math.random() * 100) / 100;

  if (Math.random() > 0.5) {
    return price + delta;
  } else {
    return price - delta;
  }
}

setInterval(function() {
  var tick = {
    "stock": 'PETR4',
    "price": addRandomVariation(21.12),
    "timestamp": Date.now()
  }

  var zmqPayloadStr = JSON.stringify(tick);

  console.log('Sending tick', zmqPayloadStr);
  pub.send(['message', zmqPayloadStr]);
}, 100);

// ticker
var zmq = require('zeromq')
  , pub = zmq.socket('pub');

pub.connect('tcp://192.168.99.100:3001');
console.log('Producer connected to port 3001');

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
    "stock": 'VAL5',
    "price": addRandomVariation(32.23),
    "timestamp": Date.now()
  }

  var zmqPayloadStr = JSON.stringify(tick);

  console.log('Sending tick', zmqPayloadStr);
  pub.send(['message', zmqPayloadStr]);
}, 3000);

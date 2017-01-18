var zmq = require('zeromq')
  , pub = zmq.socket('pub');

pub.connect('tcp://192.168.99.100:3011');
console.log('Producer connected to port 3011');

function addRandomVariationTo(price) {
  var delta = Math.round(Math.random() * 100) / 100;

  if (Math.random() > 0.5) {
    return price + delta;
  } else {
    return price - delta;
  }
}

setInterval(function() {
  var topic = "TICK.VALE5";
  var timestamp = Date.now();
  var message = {
    "price": addRandomVariationTo(29.46)
  }

  console.log('Sending tick for ', topic);
  pub.send([topic, timestamp, JSON.stringify(message)]);
}, 1000);

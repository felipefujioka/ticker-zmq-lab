var zmq = require('zeromq')
  , pub = zmq.socket('pub');

pub.connect('tcp://192.168.99.100:5001');
console.log('Producer connected to port 5001');

function addRandomVariationTo(price) {
  var delta = Math.round(Math.random() * 100) / 100;

  if (Math.random() > 0.5) {
    return price + delta;
  } else {
    return price - delta;
  }
}

var seq = 0;

setInterval(function() {
  var topic = "TICK." + process.env.SECURITY_SYMBOL;
  var timestamp = Date.now();
  var message = {
    "price": addRandomVariationTo(+process.env.BASE_PRICE),
    "seq": seq++
  }

  console.log('o: ' + topic + ' ' + timestamp + ' ' + JSON.stringify(message));
  pub.send([topic, timestamp, JSON.stringify(message)]);
}, +process.env.TICK_PERIOD);

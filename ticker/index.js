// ticker
var zmq = require('zeromq')
  , pub = zmq.socket('pub');

pub.bindSync('tcp://0.0.0.0:3000');
console.log('Producer bound to port 3000');

setInterval(function(){
  console.log('sending work');
  pub.send(['PETR4', '{"price":21.12}']);
}, 100);
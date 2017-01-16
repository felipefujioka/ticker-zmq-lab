// worker.js
var zmq = require('zeromq')
  , sub = zmq.socket('sub');

sub.connect('tcp://192.168.99.100:3001');
sub.subscribe('PETR4');

console.log('Worker connected to port 3001');

sub.on('message', function(topic, message){
  console.log('received a message related to:', topic.toString(), 'containing message:', message.toString());
});

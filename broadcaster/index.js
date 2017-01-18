// worker.js
var zmq = require('zeromq')
  , sub = zmq.socket('sub');

var io = require('socket.io')();
io.on('connection', function(client){ });
io.listen(3003);

sub.connect('tcp://192.168.99.100:3002');
sub.subscribe('TICK');

console.log('Worker connected to port 3002');

sub.on('message', function(topic, timestamp, message){
  console.log('received a message related to:', topic.toString(), 'containing message:', message.toString());
  var wrapper = {
    topic: topic.toString(), 
    timestamp: timestamp.toString(),
    message: JSON.parse(message.toString())
  };
  io.emit('message', JSON.stringify(wrapper));
});

// worker.js
var zmq = require('zeromq')
  , sub = zmq.socket('sub');

var io = require('socket.io')();
io.on('connection', function(client){ });
io.listen(3002);

sub.connect('tcp://192.168.99.100:3001');
sub.subscribe('message');

console.log('Worker connected to port 3001');

sub.on('message', function(topic, message){
  // console.log('received a message related to:', topic.toString(), 'containing message:', message.toString());
  io.emit('message', message.toString());
});

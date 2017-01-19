// worker.js
var zmq = require('zeromq')
  , sub = zmq.socket('sub');

var io = require('socket.io')();
io.on('connection', function(client){ });
io.listen(process.env.PORT);

sub.connect('tcp://192.168.99.100:' + process.env.SUB_PORT);
sub.subscribe(process.env.CHANNEL);

console.log('Worker connected to port ' + process.env.SUB_PORT);

sub.on('message', function(topic, timestamp, message){
  console.log('received a message from proxy:', process.env.SUB_PORT);
  var wrapper = {
    topic: topic.toString(), 
    timestamp: timestamp.toString(),
    message: JSON.parse(message.toString())
  };
  io.emit('message', JSON.stringify(wrapper));
});

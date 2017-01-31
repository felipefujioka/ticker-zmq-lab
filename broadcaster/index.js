// worker.js
var zmq = require('zeromq')
  , sub = zmq.socket('sub');

var io = require('socket.io')();
io.on('connection', function(client){ });
io.listen(process.env.PORT);

sub.connect('tcp://' + process.env.SUB_ADDR);
sub.subscribe(process.env.CHANNEL);

sub.on('message', function(topic, timestamp, message){
  console.log('i: ' + topic + ' ' + timestamp + ' ' + message.toString());

//  var wrapper = {
//    topic: topic.toString(),
//    timestamp: timestamp.toString(),
//    message: JSON.parse(message.toString())
//  };
//  io.emit('message', JSON.stringify(wrapper));
});

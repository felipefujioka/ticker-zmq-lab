// ticker
var zmq = require('zeromq')
  , pub = zmq.socket('pub')
  , sub = zmq.socket('sub');

sub.subscribe('message');
sub.bindSync('tcp://*:3001');
console.log('Device bound to port 3001');

pub.bindSync('tcp://*:3002');

sub.on('message', function(topic, message) {
   console.log('Repassing message related to:', topic.toString(), 'containing message:', message.toString());
   pub.send([topic, message]);
});

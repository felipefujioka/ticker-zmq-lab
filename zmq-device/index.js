// ticker
var zmq = require('zeromq')
  , pub = zmq.socket('pub')
  , sub = zmq.socket('sub');

sub.subscribe('PETR4');
sub.connect('tcp://192.168.99.100:3000');
console.log('Device bound to port 3000');

pub.bindSync('tcp://0.0.0.0:3001');

sub.on('message', function(topic, message) {
   console.log('Repassing message related to:', topic.toString(), 'containing message:', message.toString());
   pub.send([topic, message]);
});

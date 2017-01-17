// ticker
var zmq = require('zeromq')
  , pub = zmq.socket('xpub')
  , sub = zmq.socket('xsub');

// sub.subscribe('message');
sub.bindSync('tcp://*:3001');
console.log('Device bound to port 3001');

pub.setsockopt(zmq.ZMQ_XPUB_VERBOSE, 1);
pub.bindSync('tcp://*:3002');

sub.on('message', function(topic, message) {
  console.log('Repassing message related to:', topic.toString(), 'containing message:', message.toString());
  pub.send([topic, message]);
});

pub.on('message', function(data) {
  sub.send(data);
  console.log('pub sending to sub...');
})

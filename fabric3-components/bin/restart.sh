#!/bin/bash

mvn clean install
kill -9 $(lsof -t -i:5001)
kill -9 $(lsof -t -i:8183)
java -jar -DSECURITY_SYMBOL=PETR4 server_producer/target/image/bin/server.jar clean
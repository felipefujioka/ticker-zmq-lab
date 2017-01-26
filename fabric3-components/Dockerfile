FROM maven:3.2-jdk-8

ARG server_path

COPY pom.xml .

COPY server_producer/pom.xml server_producer/pom.xml
COPY zmq_producer/pom.xml zmq_producer/pom.xml

RUN mvn validate

COPY . .

RUN mvn package

CMD "java -jar $server_path clean"
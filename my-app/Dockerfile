FROM maven:3.2-jdk-7

WORKDIR /usr/src/app
COPY pom.xml .
RUN mvn install

COPY . .
RUN mvn package

CMD ["java", "-cp", "target/my-app-1.0-SNAPSHOT.jar", "com.mycompany.app.App"]

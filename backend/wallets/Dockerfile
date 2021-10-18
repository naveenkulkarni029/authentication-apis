FROM openjdk:8u302-jre AS base
WORKDIR /wallets
EXPOSE 8080

FROM maven:3.8.1-openjdk-8 as maven
COPY ./pom.xml ./pom.xml
COPY ./src ./src
RUN mvn clean install 
RUN mvn package spring-boot:repackage

FROM base
COPY --from=maven target/wallets-1.0.0-SNAPSHOT.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
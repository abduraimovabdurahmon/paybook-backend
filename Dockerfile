# Build stage
FROM maven:3.9.9-amazoncorretto-21 AS builder
LABEL authors="a.abduraimov"

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src src
RUN mvn clean package -DskipTests -B

# Runtime stage with Tomcat
FROM tomcat:10-jdk21-openjdk-slim
RUN rm -rf /usr/local/tomcat/webapps/*
COPY --from=builder /app/target/*.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
FROM openjdk

WORKDIR /app

COPY target/btc-service-0.0.1-SNAPSHOT.jar /app/btc-service.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/btc-service.jar"]
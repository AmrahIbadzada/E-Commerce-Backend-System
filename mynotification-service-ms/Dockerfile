FROM eclipse-temurin:21-jre

WORKDIR /app

COPY build/libs/mynotification-service-ms-0.0.1-SNAPSHOT-plain.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]

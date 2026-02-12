FROM openjdk:21
WORKDIR /app
COPY build/libs/myproduct-ms-0.0.1-SNAPSHOT-plain.jar /app/myproduct-ms-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "product-ms.jar"]
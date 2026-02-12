FROM openjdk:21
WORKDIR /app
COPY build/libs/mypayment-ms-0.0.1-SNAPSHOT-plain.jar /app/mypayment-ms-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "mypayment-ms.jar"]
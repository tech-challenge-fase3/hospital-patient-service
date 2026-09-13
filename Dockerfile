FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY hospital-parent/pom.xml hospital-parent/pom.xml
COPY hospital-patient-service/pom.xml hospital-patient-service/pom.xml
COPY hospital-patient-service/.mvn hospital-patient-service/.mvn
COPY hospital-patient-service/mvnw hospital-patient-service/mvnw
COPY hospital-patient-service/src hospital-patient-service/src
RUN chmod +x hospital-patient-service/mvnw \
    && ./hospital-patient-service/mvnw -f hospital-patient-service/pom.xml clean package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /workspace/hospital-patient-service/target/*SNAPSHOT.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

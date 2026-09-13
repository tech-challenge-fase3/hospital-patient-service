FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY hospital-parent/pom.xml hospital-parent/pom.xml
COPY hospital-patient-service/pom.xml hospital-patient-service/pom.xml
COPY hospital-patient-service/.mvn hospital-patient-service/.mvn
COPY hospital-patient-service/mvnw hospital-patient-service/mvnw
COPY hospital-patient-service/src hospital-patient-service/src
RUN chmod +x hospital-patient-service/mvnw \
    && ./hospital-patient-service/mvnw -f hospital-patient-service/pom.xml clean package -DskipTests \
    && cd hospital-patient-service/target \
    && jar xf app.jar META-INF/MANIFEST.MF \
    && grep -q 'Main-Class: org.springframework.boot.loader.launch.JarLauncher' META-INF/MANIFEST.MF

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /workspace/hospital-patient-service/target/app.jar /app/app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

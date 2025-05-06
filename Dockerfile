FROM maven:3-eclipse-temurin-21 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean install -DskipTests=true --no-transfer-progress
ARG artifactId
RUN echo "Build with artifactId: ${artifactId}"

FROM eclipse-temurin:21-jammy
ARG artifactId
RUN echo "After Build with MY_VAR: ${artifactId}"
COPY --from=build /home/app/target/*.jar /usr/local/lib/$artifactId.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /usr/local/lib/$artifactId.jar" ]
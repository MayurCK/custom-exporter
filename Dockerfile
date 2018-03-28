FROM openjdk:8-jdk
WORKDIR /exporter
COPY . /exporter/
EXPOSE 1234
CMD java -jar java_simple-1.0-SNAPSHOT-jar-with-dependencies.jar
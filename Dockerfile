FROM openjdk:17-jdk-alpine

LABEL version="1"
ENV TZ=Europe/Moscow

ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} watcher-0.0.1-SNAPSHOT.jar

# if you need your old base, you need copy db files into root project and uncomment 2 rows below.
#COPY ./crypto_db.mv.db ./crypto_db.mv.db
#COPY ./crypto_db.trace.db ./crypto_db.trace.db

EXPOSE 28852
ENTRYPOINT ["java","-jar","/watcher-0.0.1-SNAPSHOT.jar"]
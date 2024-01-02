FROM openjdk:11-jre

WORKDIR /app
ARG JAR_FILE
ADD target/${JAR_FILE} /app/booklib.jar
EXPOSE 443
ENV JAVA_OPTIONS "-Xms2g -Xmx6g -Dfile.encoding=UTF-8 -Dspring.profiles.active=docker -Djava.awt.headless=true -Djava.awt.graphicsenv=sun.awt.CGraphicsEnvironment "
ENV OVERRIDE_PROP ""

ENTRYPOINT ["java", "-jar", "/app/booklib.jar"]
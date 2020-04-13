FROM tomcat:9.0

COPY ["target/demo-0.0.1-SNAPSHOT.war", "webapps/"]

EXPOSE 8080
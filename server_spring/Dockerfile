FROM fcumselab/progedu-server-maven-env:latest as builder

RUN mkdir /workdir

WORKDIR /workdir

COPY . .

RUN cd src/main/resources/config/ && \
    ( [ -f course_config.properties ] && echo "get course_config.properties") \
    || (cp course_config.properties.example course_config.properties && echo "use course_config.properties.example") && \
    \
    ( [ -f db_config.properties ] && echo "get db_config.properties") \
    || (cp db_config.properties.example db_config.properties && echo "use db_config.properties.example") && \
    \
    ( [ -f gitlab_config.properties ] && echo "get gitlab_config.properties") \
    || (cp gitlab_config.properties.example gitlab_config.properties && echo "use gitlab_config.properties.example") && \
    \
    ( [ -f jenkins_config.properties ] && echo "get jenkins_config.properties") \
    || (cp jenkins_config.properties.example jenkins_config.properties && echo "use jenkins_config.properties.example")
    
# RUN apt-get update \
#     && apt-get -y install gettext-base \
#     && apt-get clean \
#     && rm -rf /var/lib/apt/lists/*

RUN mvn clean package -DskipTests

FROM openjdk:8-jdk-alpine

RUN apk update && \
    apk upgrade && \
    apk add git && \
    git config --global user.email "admin@example.com" && \
    git config --global user.name "Administrator"




COPY --from=builder /workdir/target/progedu-0.0.1-SNAPSHOT.jar app.jar

ENV application-name=progedu_server 

ENTRYPOINT ["java","-jar","/app.jar","--spring.application.name=${application-name}"]
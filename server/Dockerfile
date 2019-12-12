FROM maven as builder

RUN mkdir /workdir

WORKDIR /workdir

COPY . .

RUN cd src/main/resources/config/ && \
    [ -f course_config.properties ] && cat course_config.properties || cp course_config.properties.example course_config.properties && \
    [ -f db_config.properties ] && cat db_config.properties || cp db_config.properties.example db_config.properties && \
    [ -f gitlab_config.properties ] && cat gitlab_config.properties || cp gitlab_config.properties.example gitlab_config.properties && \
    [ -f jenkins_config.properties ] && cat jenkins_config.properties || cp jenkins_config.properties.example jenkins_config.properties

ARG WEB_API_BASEURI=
ENV WEB_API_BASEURI=$WEB_API_BASEURI

RUN apt-get update \
    && apt-get -y install gettext-base \
    && apt-get clean \
    && rm -rf /var/lib/apt/lists/* \
    && envsubst < front/src/environments/environment.docker.ts > front/src/environments/environment.prod.ts

RUN mvn -T 1C clean package -DskipTests

FROM tomcat

COPY tomcat-config/context.xml /usr/local/tomcat/webapps/manager/META-INF/context.xml
COPY tomcat-config/tomcat-users.xml /usr/local/tomcat/conf/tomcat-users.xml

ARG DEBUG=false
ENV DEBUG=$DEBUG
ENV LOG_LEVEL=$LOG_LEVEL

RUN echo $DEBUG

RUN git config --global user.email "admin@example.com" && \
    git config --global user.name "Administrator" && \
    if [ ${DEBUG} = true ]; then \
    rm -rf /usr/local/tomcat/webapps/ROOT /usr/local/tomcat/webapps/ROOT.war \
    ;else \
    rm -rf /usr/local/tomcat/webapps/ && mkdir /usr/local/tomcat/webapps/ \
    ;fi

COPY --from=builder /workdir/target/ProgEdu.war /usr/local/tomcat/webapps/ROOT.war

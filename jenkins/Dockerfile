FROM maven:3-jdk-8 as builder

RUN mkdir /workdir

RUN apt update && apt install -yqq git

WORKDIR /workdir

ARG GITLAB_USER
ARG GITLAB_TOKEN

RUN git clone http://$GITLAB_USER:$GITLAB_TOKEN@mselab.iecs.fcu.edu.tw:20080/root/ProgEduJenkinsPlugin.git
RUN git clone http://$GITLAB_USER:$GITLAB_TOKEN@mselab.iecs.fcu.edu.tw:20080/angus850503/screenshotjenkinsplugin.git
RUN git clone http://$GITLAB_USER:$GITLAB_TOKEN@mselab.iecs.fcu.edu.tw:20080/angus850503/UpdatePngToDBJenkinsPlugin.git

RUN cd ProgEduJenkinsPlugin && mvn package
RUN cd screenshotjenkinsplugin && mvn package
RUN cd UpdatePngToDBJenkinsPlugin && mvn package

FROM jenkins/jenkins:lts

USER root

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY --from=builder /workdir/ProgEduJenkinsPlugin/target/jenkins-progedu.hpi /usr/share/jenkins/ref/plugins/jenkins-progedu.jpi
COPY --from=builder /workdir/screenshotjenkinsplugin/target/screenshot.hpi /usr/share/jenkins/ref/plugins/screenshot.jpi
COPY --from=builder /workdir/UpdatePngToDBJenkinsPlugin/target/progeduDB.hpi /usr/share/jenkins/ref/plugins/progeduDB.jpi

RUN curl -sL https://deb.nodesource.com/setup_10.x | bash - \
    && \
    apt-get update && apt-get install -y --no-install-recommends \
    nodejs \
    && \
    apt-get clean \
    && \
    rm -rf /var/lib/apt/lists/*

ARG GRADLE_MAVEN=5.6.2
ENV PATH=$PATH:/opt/gradle/gradle-$GRADLE_MAVEN/bin
RUN apt update && apt install -y default-jdk maven && \
    wget https://services.gradle.org/distributions/gradle-$GRADLE_MAVEN-bin.zip && \
    mkdir /opt/gradle && \
    unzip -d /opt/gradle gradle-$GRADLE_MAVEN-bin.zip && \
    rm gradle-$GRADLE_MAVEN-bin.zip \
    && \
    apt-get clean \
    && \
    rm -rf /var/lib/apt/lists/*

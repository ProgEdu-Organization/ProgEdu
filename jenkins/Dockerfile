
###########################################################################
# Jenkins Install plugin:
###########################################################################

FROM maven:3-jdk-8 as builder

RUN mkdir /workdir

RUN apt-get update && apt-get install -yqq git

WORKDIR /workdir

RUN git clone https://github.com/fcumselab/screenshotjenkinsplugin.git
RUN git clone https://github.com/fcumselab/UpdatePngToDBJenkinsPlugin.git
RUN git clone https://github.com/fcumselab/ProgEduJenkinsPlugin.git
RUN git clone https://github.com/fcumselab/AndroidScreenshotPlugin.git

RUN cd ProgEduJenkinsPlugin && mvn package
RUN cd screenshotjenkinsplugin && mvn package
RUN cd UpdatePngToDBJenkinsPlugin && mvn package
RUN cd AndroidScreenshotPlugin && mvn package

FROM jenkins/jenkins:lts

USER root

RUN apt-get update && apt-get install -y apt-transport-https \
       ca-certificates curl gnupg2 \
       software-properties-common

RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -
RUN apt-key fingerprint 0EBFCD88
RUN add-apt-repository \
       "deb [arch=amd64] https://download.docker.com/linux/debian \
       $(lsb_release -cs) stable"
RUN apt-get update && apt-get install -y docker-ce-cli
USER jenkins
RUN jenkins-plugin-cli --plugins blueocean:1.24.4

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY --from=builder /workdir/ProgEduJenkinsPlugin/target/jenkins-progedu.hpi /usr/share/jenkins/ref/plugins/jenkins-progedu.jpi
COPY --from=builder /workdir/screenshotjenkinsplugin/target/screenshot.hpi /usr/share/jenkins/ref/plugins/screenshot.jpi
COPY --from=builder /workdir/UpdatePngToDBJenkinsPlugin/target/progeduDB.hpi /usr/share/jenkins/ref/plugins/progeduDB.jpi
COPY --from=builder /workdir/AndroidScreenshotPlugin/target/android-screenshot.hpi /usr/share/jenkins/ref/plugins/android-screenshot.jpi



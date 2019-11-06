FROM maven:3-jdk-8 as builder

RUN mkdir /workdir

RUN apt-get update && apt-get install -yqq git

WORKDIR /workdir

RUN git clone https://github.com/fcumselab/UpdatePngToDBJenkinsPlugin.git
RUN git clone https://github.com/fcumselab/ProgEduJenkinsPlugin.git

RUN cd ProgEduJenkinsPlugin && mvn package
RUN cd UpdatePngToDBJenkinsPlugin && mvn package

FROM jenkins/jenkins:lts

USER root

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt

RUN /usr/local/bin/install-plugins.sh < /usr/share/jenkins/ref/plugins.txt

COPY --from=builder /workdir/ProgEduJenkinsPlugin/target/jenkins-progedu.hpi /usr/share/jenkins/ref/plugins/jenkins-progedu.jpi
COPY --from=builder /workdir/UpdatePngToDBJenkinsPlugin/target/progeduDB.hpi /usr/share/jenkins/ref/plugins/progeduDB.jpi

SHELL ["/bin/bash", "-c"]

###########################################################################
# Install Library:
###########################################################################

RUN dpkg --add-architecture i386 \
		&& \
	apt-get update && apt-get install -y --no-install-recommends \
		&& \
	apt-get install -y libc6-dev-i386 libc6-i386 zlib1g:i386 libstdc++6:i386  libxcomposite-dev libxcursor-dev:i386 \
		&& \
	apt-get install -y lib32gcc1 lib32ncurses5 lib32z1 libqt5widgets5 libpulse0:i386 pulseaudio libnss3-dev \
		&& \
	apt-get clean \
		&& \
  rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

###########################################################################
# Install Android SDK:
###########################################################################

# Environment variables
ARG SDK_NAME=sdk-tools-linux-4333796
ENV ANDROID_HOME /usr/local/android-sdk-linux
ENV ANDROID_SDK_HOME /usr/local/android-sdk-linux
ENV ANDROID_AVD_HOME /root/.android/avd
ENV PATH $PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$ANDROID_HOME/emulator

RUN cd /usr/local \ 
		&& \
	mkdir -p $ANDROID_HOME \
		&& \
	cd $ANDROID_HOME \
		&& \
	wget https://dl.google.com/android/repository/$SDK_NAME.zip -O sdk.zip -q \
		&& \
	unzip sdk.zip >> /dev/null \
		&& \
	rm sdk.zip \
		&& \
	mkdir -p $ANDROID_HOME/.android \
		&& \
	touch $ANDROID_HOME/.android/repositories.cfg

###########################################################################
# Setup Android SDK:
###########################################################################

ARG SDK_SETUP=false
ARG SDK_PLATFORMS
ARG SDK_BUILD_TOOLS

RUN if [ ${SDK_SETUP} = true ]; then \
  yes | $ANDROID_HOME/tools/bin/sdkmanager "platforms;$SDK_PLATFORMS" >> /dev/null \
		&& \
	yes | $ANDROID_HOME/tools/bin/sdkmanager "build-tools;$SDK_BUILD_TOOLS" >> /dev/null \
		&& \
	yes | $ANDROID_HOME/tools/bin/sdkmanager "platform-tools" >> /dev/null \
		&& \
	yes | $ANDROID_HOME/tools/bin/sdkmanager "emulator" >> /dev/null \
;fi

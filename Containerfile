FROM docker.io/ubuntu:jammy

ARG DEBIAN_FRONTEND=noninteractive
ARG TZ=Asia/Hong_Kong

RUN apt-get update && \
	apt-get install -y \
	
	# 
	# Oracle JDK 17 dependencies
	# 
	
	libasound2 \
	libc6-i386 \
	libc6-x32 \
	libfreetype6 \
	libx11-6 \
	libxext6 \
	libxi6 \
	libxrender1 \
	libxtst6 \
	
	# 
	# Chrome dependencies
	# 
	
	libdrm2 \
	libexpat1 \
	libgbm1 \
	libglib2.0-0 \
	libgtk-3-0 \
	libnspr4 \
	libnss3 \
	libpango-1.0-0 \
	libu2f-udev \
	libvulkan1 \
	libx11-6 \
	libxcb1 \
	libxcomposite1 \
	libxdamage1 \
	libxext6 \
	libxfixes3 \
	libxkbcommon0 \
	libxrandr2 \
	xdg-utils \
	fonts-liberation \
	libasound2 \
	libatk-bridge2.0-0 \
	libatk1.0-0 \
	libatspi2.0-0 \
	libcairo2 \
	libcups2 \
	libcurl3-gnutls \
	libdbus-1-3 \
	
	curl \
	unzip \
	gpg \
	tar  \
	wget && \
	rm -rf /var/lib/apt/lists/*

# Download and install the oracle jdk 17.0.6.
RUN wget -P \
	/tmp/ \
	https://download.oracle.com/java/17/archive/jdk-17.0.6_linux-x64_bin.deb && \
	apt install -y \
	/tmp/jdk-17.0.6_linux-x64_bin.deb

# Set up java env
ENV JAVA_HOME=/usr/lib/jvm/jdk-17
ENV PATH=${JAVA_HOME}/bin:$PATH

RUN wget -P \
	/tmp/ \
	https://dl.google.com/linux/direct/google-chrome-stable_current_amd64.deb && \
	apt install -y \
	/tmp/google-chrome-stable_current_amd64.deb
FROM ubuntu:22.04

# install jdk 17
RUN apt update && apt install -y openjdk-17-jdk && apt install -y openjdk-17-jre

# install java
RUN apt install -y libc6-x32 libc6-i386 wget
RUN wget https://download.oracle.com/java/17/latest/jdk-17_linux-x64_bin.deb
RUN dpkg -i jdk-17_linux-x64_bin.deb
# RUN update-alternatives --install /usr/bin/java java /usr/lib/jvm/jdk-17/bin/java 1
RUN apt-get update && apt-get -y install maven && apt-get clean
RUN apt-get install haveged -y

# copy the project code and install dependencies
WORKDIR /app
ADD . /app
RUN mvn -N io.takari:maven:wrapper
RUN mvn install -DskipTests

CMD ["mvn","spring-boot:run", "-Dspring.application.admin.enabled=true"]
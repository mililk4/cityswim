FROM ubuntu:14.04
Maintainer William Pietri <william-cityswim-201503@scissor.com>

#
RUN apt-get update
RUN apt-get install -y openjdk-7-jre-headless

ADD target/scala-2.10/cityswim.jar /usr/local/bin/cityswim.jar

run mkdir /var/www/cityswim
run mkdir /var/www/cityswim/v1

VOLUME /var/www/cityswim

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/local/bin/cityswim.jar"]
CMD []

FROM java:8
VOLUME /tmp
ADD /build/libs/orange-0.0.1-SNAPSHOT.jar orange.jar
ENTRYPOINT ["java","-jar","/orange.jar"]
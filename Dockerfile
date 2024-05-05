FROM openjdk:17
EXPOSE 8080
ADD target/authservice-onemorerep.jar authservice-onemorerep.jar
ENTRYPOINT ["java","-jar","/authservice-onemorerep.jar"]
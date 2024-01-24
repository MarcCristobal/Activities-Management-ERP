
# Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
# Click nbfs://nbhost/SystemFileSystem/Templates/Other/Dockerfile to edit this template

FROM openjdk:17
ADD target/activities-docker.jar activities-docker.jar
ENTRYPOINT ["java","-jar","/activities-docker.jar"]
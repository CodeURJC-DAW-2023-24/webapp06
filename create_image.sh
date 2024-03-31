#!/bin/bash
cd ./

sudo docker build -t jk2024/spring-boot-daw:latest -f docker/DockerfileSpring .

sudo docker push jk2024/spring-boot-daw:latest
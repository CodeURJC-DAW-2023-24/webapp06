#!/bin/bash
cd ./

sudo docker build -t jakurudev/spring-boot-daw:latest -f docker/DockerfileSpring .

sudo docker push jakurudev/spring-boot-daw:latest
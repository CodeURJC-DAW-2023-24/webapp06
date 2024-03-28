#!/bin/bash
cd ./

docker build -t jakurudev/spring-boot-daw:latest -f docker/DockerfileSpring .

docker push jakurudev/spring-boot-daw:latest
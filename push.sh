#!/bin/bash
echo "Building image..."
gradle bootRepackage -Pprod buildDocker && docker tag jhipster:latest localhost:5000/biziapp:latest && docker push localhost:5000/biziapp:latest
docker build -t localhost:5000/hive-server:latest ./docker-hive/ && docker push localhost:5000/hive-server:latest

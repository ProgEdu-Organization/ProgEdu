#!/bin/bash

if [ -f "./.env" ]; then
    . ./.env
    progedu_jenkins_container_name=${COMPOSE_PROJECT_NAME}_jenkins_1
    sudo docker exec -it ${progedu_jenkins_container_name} cat /var/jenkins_home/secrets/initialAdminPassword
else
    echo "not exist .env" && exit 1
fi

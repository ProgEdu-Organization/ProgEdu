#!/bin/sh

if [ -f "./.env" ]; then

    # replace all \r at end, becasue CRLF format in .env will cause error
    sed -i 's/\r$//' .env
    
    # as same as `source .env` in bash, that is run .env then get the key/value 
    . ./.env 
    
    progedu_jenkins_container_name=${COMPOSE_PROJECT_NAME}_jenkins_1
    sudo docker exec -it ${progedu_jenkins_container_name} cat /var/jenkins_home/secrets/initialAdminPassword
else
    echo "not exist .env" && exit 1
fi

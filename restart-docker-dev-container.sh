#!/usr/bin/env bash

is_running=`docker ps | grep ekanban-$1_1`
if [ -n "$is_running" ]; then
    docker-compose -f docker-compose.dev.yml -p ekanbandev stop ekanban-$1
fi

if [ "$1" == "backend" ]; then
    docker-compose -f docker-compose.dev.yml -p ekanbandev stop ekanban-mongodb
    docker-compose -f docker-compose.dev.yml -p ekanbandev rm -f ekanban-mongodb
fi

if [ -z "$1" ]; then
    echo -e "\n \033[1mUsage: ./restart-docker-dev-container.sh container-name (frontend or backend)\033[m\n"
    exit
fi

docker-compose -f docker-compose.dev.yml -p ekanbandev rm -f ekanban-$1
docker-compose -f docker-compose.dev.yml -p ekanbandev up -d --build --force-recreate ekanban-$1

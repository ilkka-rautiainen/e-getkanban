#!/usr/bin/env bash

docker-compose -f docker-compose.dev.yml -p ekanban-dev stop
docker-compose -f docker-compose.dev.yml -p ekanban-dev rm -f
docker-compose -f docker-compose.dev.yml -p ekanban-dev up --build --force-recreate

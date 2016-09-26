#!/usr/bin/env bash

docker-compose -f docker-compose.prod.yml -p ekanban-prod stop
docker-compose -f docker-compose.prod.yml -p ekanban-prod rm -f
docker-compose -f docker-compose.prod.yml -p ekanban-prod up --build --force-recreate

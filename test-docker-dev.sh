#!/usr/bin/env bash

docker-compose -f docker-compose.dev.yml -f docker-compose.test.yml -p ekanban-test stop
docker-compose -f docker-compose.dev.yml -f docker-compose.test.yml -p ekanban-test rm -f
docker-compose -f docker-compose.dev.yml -f docker-compose.test.yml -p ekanban-test up --force-recreate

#!/usr/bin/env bash

docker-compose -f docker-compose.dev.yml stop
docker-compose -f docker-compose.dev.yml rm -f
docker-compose -f docker-compose.dev.yml up --build --force-recreate

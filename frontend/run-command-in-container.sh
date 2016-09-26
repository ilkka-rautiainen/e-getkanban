#!/usr/bin/env bash

docker-compose -f ../docker-compose.dev.yml -f ../docker-compose.dev.frontend-sleep.yml -p ekanban-run-command-in-frontend stop ekanban-frontend
docker-compose -f ../docker-compose.dev.yml -f ../docker-compose.dev.frontend-sleep.yml -p ekanban-run-command-in-frontend rm -f ekanban-frontend
docker-compose -f ../docker-compose.dev.yml -f ../docker-compose.dev.frontend-sleep.yml -p ekanban-run-command-in-frontend up --build --force-recreate -d ekanban-frontend
docker exec ekanbanruncommandinfrontend_ekanban-frontend_1 "$@"
docker-compose -f ../docker-compose.dev.yml -f ../docker-compose.dev.frontend-sleep.yml -p ekanban-run-command-in-frontend stop ekanban-frontend
docker-compose -f ../docker-compose.dev.yml -f ../docker-compose.dev.frontend-sleep.yml -p ekanban-run-command-in-frontend rm -f ekanban-frontend

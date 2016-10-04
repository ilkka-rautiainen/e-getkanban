#!/usr/bin/env bash

# Note: might give the error: Error response from daemon: No such container: ekanban-run-command-in-frontend
docker stop ekanban-run-command-in-frontend
docker rm -f ekanban-run-command-in-frontend

docker build -t ekanban-run-command-in-frontend -f Dockerfile-dev .
docker run -v `pwd`:/usr/src/app -d --name ekanban-run-command-in-frontend ekanban-run-command-in-frontend
docker exec ekanban-run-command-in-frontend "$@"

docker stop ekanban-run-command-in-frontend
docker rm -f ekanban-run-command-in-frontend

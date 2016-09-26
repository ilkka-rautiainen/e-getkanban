#!/usr/bin/env bash

set -e

function run_tests {
    echo -e "\033[1mRunning $1 tests...\033[m"
    docker-compose -f docker-compose.test.ci.yml -p ekanban-test-ci stop "$1" >/dev/null
    docker-compose -f docker-compose.test.ci.yml -p ekanban-test-ci rm -f "$1" >/dev/null
    docker-compose -f docker-compose.test.ci.yml -p ekanban-test-ci up --build --force-recreate -d "$1" >/dev/null
    return_value=`docker wait ekanbantestci_$1_1`
    if [ "$return_value" != "0" ]; then
      echo -e "\033[1;31m$1 tests FAILED:\033[m"
      docker-compose -f docker-compose.test.ci.yml -p ekanban-test-ci logs "$1"
      echo -e "\033[1;31m$1 tests FAILED\033[m"
      exit $return_value
    else
      echo -e "\033[1;32m$1 tests PASSED\033[m"
    fi
}

run_tests ekanban-frontend
run_tests ekanban-backend
docker-compose -f docker-compose.test.ci.yml -p ekanban-test-ci stop >/dev/null
echo -e "\033[1;32mALL TESTS PASSED\033[m"

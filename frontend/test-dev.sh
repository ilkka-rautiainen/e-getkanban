
#!/usr/bin/env bash

docker-compose -f ../docker-compose.test.dev.frontend.yml -p ekanban-test-dev-frontend stop ekanban-frontend >/dev/null
docker-compose -f ../docker-compose.test.dev.frontend.yml -p ekanban-test-dev-frontend rm -f ekanban-frontend >/dev/null
docker-compose -f ../docker-compose.test.dev.frontend.yml -p ekanban-test-dev-frontend up --force-recreate ekanban-frontend

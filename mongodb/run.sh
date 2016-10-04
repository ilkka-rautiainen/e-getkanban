#!/usr/bin/env bash

/usr/bin/mongod --dbpath /data --nojournal &
while ! nc -vz localhost 27017; do sleep 1; done
mongo ekanban --eval "db.createUser({ user: 'ekanban', pwd: 'nabnake', roles: [ { role: 'dbOwner', db: 'ekanban' } ] });"
mongo ekanban-test --eval "db.createUser({ user: 'ekanban', pwd: 'nabnake', roles: [ { role: 'dbOwner', db: 'ekanban-test' } ] });"
/usr/bin/mongod --dbpath /data --shutdown
/usr/bin/mongod --dbpath /data --auth

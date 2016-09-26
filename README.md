# Setting up development environment

1. Install `docker` (>= 1.12.1) and `docker-compose` (>= 1.8.0)
2. In `/frontend` folder, run `./run-command-in-container.sh npm install`
3. In root, run `./start-dev.sh`

# Running tests

### In CI
Run `./test-ci.sh`

### In development
- Frontend: (in frontend folder)
    - Run `./test-dev.sh` (inside docker) OR
    - Run `npm test` (on host, might not work if platform-dependent packages are installed)
- Backend: (in backend folder) run `./gradlew build`

# Running in production
Run `./start-prod.sh`

# Adding platform-dependent frontend dependecies on non-linux host
Run the `npm install ...` command through `./run-command-in-container.sh npm install ...` in `/frontend` folder

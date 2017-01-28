# Setting up development environment

1. Install `docker` (>= 1.12.1) and `docker-compose` (>= 1.8.0)
2. In `/frontend` folder, run `./run-command-in-container.sh npm install`
3. In root, run `./start-dev.sh`

# Running tests

## Frontend
No tests. Some e2e tests could be done.

## Backend
Backend tests are run in the IntelliJ Idea IDE or from command line.

From command line: `./gradlew TASK`

The different tasks:

- `test`: unit tests
- `integrationTest`: integration tests
- `acceptanceTest`: acceptance tests (Cucumber)
- `build`: all the above together

# Running in production
See the separate server documentation file provided.

# Adding platform-dependent frontend dependecies on non-linux host
Run the `npm install ...` command through `./run-command-in-container.sh npm install ...` in `/frontend` folder

# Object building

Creating of objects happens with builder-pattern.
Background and how to use them can be read here: [Builder-pattern](http://www.javaworld.com/article/2074938/core-java/too-many-parameters-in-java-methods-part-3-builder-pattern.html)

For fast generation of builder objects for classes in IntelliJ IDEA: [Builder-generator](https://plugins.jetbrains.com/idea/plugin/6585-builder-generator)

## Examples for test

Basic with defaults example:
```java
String playerName = "player";
game = GameBuilder.aGame()
    .withNormalDifficultyMockDefaults(playerName)
    .build();
```

Overwriting some attribute but keeping others default:
```java
String playerName = "player";
game = GameBuilder.aGame()
    .withNormalDifficultyMockDefaults(playerName)
    .withId("id")
    .build();
```

Object persistance in dev and integration-testing:
```java
@Autowired
private GameRepository gameRepository;
...
String playerName = "player";
game = GameBuilder.aGame()
    .withNormalDifficultyMockDefaults(playerName)
    .create(gameRepository);
```

**NOTE** create is not default provided by builder generator




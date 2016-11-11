package fi.aalto.ekanban.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import static fi.aalto.ekanban.ApplicationConstants.APPLICATION_JSON_TYPE;
import static fi.aalto.ekanban.ApplicationConstants.PLAY_TURN_PATH;
import static fi.aalto.ekanban.ApplicationConstants.GAME_PATH;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import fi.aalto.ekanban.models.db.games.Game;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import fi.aalto.ekanban.SpringIntegrationTest;
import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.utils.TestGameContainer;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.repositories.GameRepository;

@RunWith(HierarchicalContextRunner.class)
public class GameControllerTest extends SpringIntegrationTest {

    private Response response;

    @Autowired
    private GameRepository gameRepository;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        resetDb();
    }

    @After
    public void resetDb() {
        gameRepository.deleteAll();
    }

    public class createGame {

        public class withValidRequest {
            @Before
            public void doRequest() {
                response = given()
                            .formParam("playerName", "Player")
                            .formParam("difficultyLevel", GameDifficulty.NORMAL)
                        .when().post(GAME_PATH);
            }

            @Test
            public void shouldReturn200() { response.then().statusCode(200); }

            @Test
            public void shouldReturnNewGame() {
                response.then().body(not(isEmptyOrNullString()));
            }
        }

        public class withoutPlayerName {
            @Before
            public void doRequest() {response = given().formParam("difficultyLevel", GameDifficulty.NORMAL).when().post(GAME_PATH); }

            @Test
            public void shouldReturn400() { response.then().statusCode(400); }

            @Test
            public void shouldReturnPlayerNameNotFoundInErrorDescription() {
                response.then().body("message", equalTo("Required String parameter 'playerName' is not present"));
            }
        }

        public class withTooLongPlayerName {
            @Before
            public void doRequest() {
                String tooLongPlayerName = RandomStringUtils.randomAscii(130);
                response = given()
                        .formParam("playerName", tooLongPlayerName)
                        .formParam("difficultyLevel", GameDifficulty.NORMAL)
                        .when().post(GAME_PATH);
            }

            @Test
            public void shouldReturn400() { response.then().statusCode(400); }

            @Test
            public void shouldReturnNameTooLong() {
                response.then().body("message", equalTo("Name should be at most 128 characters long\n"));
            }
        }

        public class withoutGameDifficulty {
            @Before
            public void doRequest() { response = given().formParam("playerName", "Player").when().post(GAME_PATH); }

            @Test
            public void shouldReturn400() { response.then().statusCode(400); }

            @Test
            public void shouldReturnDifficultyLevelNotFoundInErrorDescription() {
                response.then().body("message", equalTo("Required GameDifficulty parameter 'difficultyLevel' is not present"));
            }
        }

        public class withInvalidGameDifficulty {
            @Before
            public void doRequest() {
                response = given()
                        .formParam("playerName", "Player")
                        .formParam("difficultyLevel", "thisisntagamedifficultyatall")
                        .when().post(GAME_PATH);
            }

            @Test
            public void shouldReturn400() { response.then().statusCode(400); }

            @Test
            public void shouldReturnDifficultyLevelNotFoundInErrorDescription() {
                response.then().body("message",
                        containsString("Failed to convert value of type [java.lang.String] to required type"));
            }
        }

    }

    public class playTurn {

        private TestGameContainer normalDifficultyGameContainer;

        @Before
        public void createInitialGame() {
            normalDifficultyGameContainer = TestGameContainer.withNormalDifficultyGame();
            gameRepository.save(normalDifficultyGameContainer.getGame());
        }

        private Turn getValidTurn() {
            return TurnBuilder
                    .aTurn()
                    .build();
        }

        public class withValidRequest {

            @Before
            public void doRequest() {
                response = given()
                        .contentType(APPLICATION_JSON_TYPE)
                        .pathParam("gameId", normalDifficultyGameContainer.getGame().getId())
                        .body(getValidTurn())
                        .when().put(GAME_PATH + PLAY_TURN_PATH);
            }

            @Test
            public void shouldReturn200() {
                response.then().statusCode(200);
            }

            @Test
            public void shouldReturnGameWithTurnPlayed() {
                response.then().body(not(isEmptyOrNullString()));
            }
        }

        public class withInvalidGameId {
            @Before
            public void doRequest() {
                response = given()
                        .contentType(APPLICATION_JSON_TYPE)
                        .pathParam("gameId", "invalidGameId")
                        .body(getValidTurn())
                        .when().put(GAME_PATH + PLAY_TURN_PATH);
            }

            @Test
            public void shouldReturn404() {
                response.then().statusCode(404);
            }

            @Test
            public void shouldContainGameNotFoundMessage() {
                response.then().body("message", equalTo("Game wasn't found"));
            }
        }

        public class withInvalidTurn {
            @Before
            public void doRequest() {
                Game gameInsteadOfATurn = normalDifficultyGameContainer.getGame();
                response = given()
                        .contentType(APPLICATION_JSON_TYPE)
                        .pathParam("gameId", normalDifficultyGameContainer.getGame().getId())
                        .body(gameInsteadOfATurn)
                        .when().put(GAME_PATH + PLAY_TURN_PATH);
            }

            @Test
            public void shouldReturn400() {
                response.then().statusCode(400);
            }
        }

        public class withMissingTurn {
            @Before
            public void doRequest() {
                response = given()
                        .contentType(APPLICATION_JSON_TYPE)
                        .pathParam("gameId", normalDifficultyGameContainer.getGame().getId())
                        .when().put(GAME_PATH + PLAY_TURN_PATH);
            }

            @Test
            public void shouldReturn400() {
                response.then().statusCode(400);
            }
        }

    }

}

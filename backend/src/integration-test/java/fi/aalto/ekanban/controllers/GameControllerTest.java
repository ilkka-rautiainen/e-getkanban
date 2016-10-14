package fi.aalto.ekanban.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static fi.aalto.ekanban.ApplicationConstants.GAME_PATH;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import fi.aalto.ekanban.SpringIntegrationTest;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.repositories.GameRepository;

import java.util.Random;

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
                logger.info(response.prettyPrint());
                response.then().body(is(notNullValue()));
            }
        }

        public class withoutPlayerName {
            @Before
            public void doRequest() {response = given().formParam("difficultyLevel", GameDifficulty.NORMAL).when().post(GAME_PATH); }

            @Test
            public void shouldReturn400() { logger.info(response.prettyPrint()); response.then().statusCode(400); }

            @Test
            public void shouldReturnPlayerNameNotFoundInErrorDescription() {
                response.then().body("message", equalTo("Required String parameter 'playerName' is not present"));
            }
        }

        public class withTooLongPlayerName {
            @Before
            public void doRequest() {
                char[] chars = "abcdefghijklmnopqrstuvwxyz".toCharArray();
                Random random = new Random();
                StringBuilder sb = new StringBuilder();
                for (Integer i=0; i<130; i++) {
                    char c = chars[random.nextInt(chars.length)];
                    sb.append(c);
                }
                String tooLongPlayerName = sb.toString();
                response = given()
                        .formParam("playerName", tooLongPlayerName)
                        .formParam("difficultyLevel", GameDifficulty.NORMAL)
                        .when().post(GAME_PATH);
            }

            @Test
            public void shouldReturn400() { logger.info(response.prettyPrint()); response.then().statusCode(400); }

            @Test
            public void shouldReturnPlayerNameNotFoundInErrorDescription() {
                response.then().body("message", equalTo("Name should be at most 128 characters long"));
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

}

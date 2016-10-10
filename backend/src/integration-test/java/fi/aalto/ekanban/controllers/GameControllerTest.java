package fi.aalto.ekanban.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

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

@RunWith(HierarchicalContextRunner.class)
public class GameControllerTest extends SpringIntegrationTest {

    private static final String GAMES = "/games";

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
                        .when().post(GAMES);
            }

            @Test
            public void shouldReturnNewGame() {
                logger.info(response.prettyPrint());
                response.then()
                        .statusCode(200)
                        .body(is(notNullValue()));
            }
        }

        public class withoutPlayerName {
            @Before
            public void doRequest() {response = given().formParam("difficultyLevel", GameDifficulty.NORMAL).when().post(GAMES); }

            @Test
            public void shouldReturn400() { response.then().statusCode(400); }

            @Test
            public void shouldReturnPlayerNameNotFoundInErrorDescription() {
                response.then().body("message", equalTo("Required String parameter 'playerName' is not present"));
            }
        }

        public class withoutGameDifficulty {
            @Before
            public void doRequest() { response = given().formParam("playerName", "Player").when().post(GAMES); }

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
                        .when().post(GAMES);
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

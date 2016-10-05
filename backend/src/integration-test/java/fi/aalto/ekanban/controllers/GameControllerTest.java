package fi.aalto.ekanban.controllers;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;
import java.util.List;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.models.games.Game;
import fi.aalto.ekanban.models.games.Card;
import fi.aalto.ekanban.models.games.EventCard;
import fi.aalto.ekanban.models.games.Phase;
import fi.aalto.ekanban.repositories.GameRepository;

@RunWith(HierarchicalContextRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameControllerTest {

    private static final Logger logger =
            LoggerFactory.getLogger(GameControllerTest.class);

    private Response response;

    @Autowired
    private GameRepository gameRepository;

    @Value("${local.server.port}")
    int port;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
        RestAssured.port = port;
        resetDb();
    }

    @After
    public void resetDb() {
        gameRepository.deleteAll();
    }

    public class getAllGames {

        private static final String GAMES = "/games";

        private Game createdGame;

        @Before
        public void initGames() {
            List<Card> backlogDeck = Arrays.asList(CardBuilder.aCard().build());
            List<EventCard> eventCardDeck = Arrays.asList(EventCardBuilder.anEventCard().build());
            List<Phase> phases = Arrays.asList(
                    PhaseBuilder.aPhase().analysis().build(),
                    PhaseBuilder.aPhase().development().build(),
                    PhaseBuilder.aPhase().test().build(),
                    PhaseBuilder.aPhase().deployed().build()
            );

            createdGame = GameBuilder.aGame()
                .withPlayerName("player")
                .withBoard(
                        BoardBuilder.aBoard()
                            .withBacklogDeck(backlogDeck)
                            .withEventCardDeck(eventCardDeck)
                            .withPhases(phases)
                            .build())
                .create(gameRepository);
        }

        public class whenNoParametersGiven {

            @Before
            public void doRequest() {
                response = when().get(GAMES);
            }

            @Test
            public void shouldReturnAllGames() {
                logger.info(response.prettyPrint());
                response.then()
                    .statusCode(200)
                    .body("[0].id", equalTo(createdGame.getId()))
                    .body("[0].playerName", equalTo(createdGame.getPlayerName()));
            }

        }

    }

}

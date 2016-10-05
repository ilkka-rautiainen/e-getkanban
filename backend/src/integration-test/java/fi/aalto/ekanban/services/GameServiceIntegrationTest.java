package fi.aalto.ekanban.services;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

import fi.aalto.ekanban.models.games.Game;

@RunWith(HierarchicalContextRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameServiceIntegrationTest {

    @Autowired
    private GameService gameService;

    @Before
    public void setUp() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

    public class startGame {

        @Test
        public void shouldReturnFreshGame() {
            Game newGame = gameService.startGame();
            //TODO change this in later branches
            assertThat(newGame, is(nullValue()));
        }

    }

}

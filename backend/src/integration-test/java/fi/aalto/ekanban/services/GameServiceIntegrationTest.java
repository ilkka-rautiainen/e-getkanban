package fi.aalto.ekanban.services;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.SpringIntegrationTest;
import fi.aalto.ekanban.models.games.Game;

@RunWith(HierarchicalContextRunner.class)
public class GameServiceIntegrationTest extends SpringIntegrationTest {

    @Autowired
    private GameService gameService;

    public class startGame {

        @Test
        public void shouldReturnFreshGame() {
            Game newGame = gameService.startGame();
            //TODO change this in later branches
            assertThat(newGame, is(nullValue()));
        }

    }

}

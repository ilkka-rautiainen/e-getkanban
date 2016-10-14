package fi.aalto.ekanban.services;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.SpringIntegrationTest;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.repositories.GameRepository;

@RunWith(HierarchicalContextRunner.class)
public class GameServiceIntegrationTest extends SpringIntegrationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    public class startGame {

        private Game newGame;
        private String playerName;
        private GameDifficulty gameDifficulty;

        public class whenDifficultyIsNormal {

            private Long gameCountBeforeAction;

            @Before
            public void setupGame() {
                gameCountBeforeAction = gameRepository.count();
                gameDifficulty = GameDifficulty.NORMAL;
                playerName = "Player";
                newGame = gameService.startGame(playerName, gameDifficulty);
            }

            @Test
            public void shouldReturnCreatedGame() {
                assertThat(gameRepository.count(), equalTo(gameCountBeforeAction + 1));
                assertThat(newGame, is(notNullValue()));
            }

        }

    }

}

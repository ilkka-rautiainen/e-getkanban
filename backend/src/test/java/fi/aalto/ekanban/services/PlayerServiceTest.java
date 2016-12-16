package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.services.turnplayers.NormalTurnPlayerService;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class PlayerServiceTest {

    private static PlayerService playerService;

    private TestGameContainer initialGameContainer;
    private Game turnPlayedGame;
    private Integer currentDayBefore;
    private Integer currentDayAfter;

    @BeforeClass
    public static void initService() {
        NormalTurnPlayerService normalTurnPlayerService = Mockito.mock(NormalTurnPlayerService.class);
        Mockito.when(normalTurnPlayerService.playTurn(Mockito.any(Game.class), Mockito.any(Turn.class)))
                .thenReturn(TestGameContainer.withNormalDifficultyMockGame().getGame());
        playerService = new PlayerService(normalTurnPlayerService);
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
        currentDayBefore = initialGameContainer.getGame().getCurrentDay();
    }

    public class playTurn {
        public class withValidTurn {
            private Turn validTurn;

            @Before
            public void initAndDoAction() {
                validTurn = TurnBuilder.aTurn().build();
                playTurn(validTurn);
            }

            @Test
            public void shouldIncrementCurrentDayByOne() {
                assertThat(currentDayAfter, equalTo(currentDayBefore + 1));
            }

        }

        public class withInvalidTurn {
            private Turn invalidTurn;

            @Before
            public void initAndDoAction() {
                playTurn(invalidTurn);
            }

            @Test
            public void shouldNotIncrementCurrentDay() {
                assertThat(currentDayAfter, equalTo(currentDayBefore));
            }
        }
    }

    private void playTurn(Turn turn) {
        turnPlayedGame = playerService.playTurn(initialGameContainer.getGame(), turn);
        currentDayAfter = turnPlayedGame.getCurrentDay();
    }
}

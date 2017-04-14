package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.services.turnplayers.MediumTurnPlayerService;
import fi.aalto.ekanban.services.turnplayers.NormalTurnPlayerService;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class PlayerServiceTest {

    private static PlayerService playerService;
    private static NormalTurnPlayerService normalTurnPlayerService;
    private static MediumTurnPlayerService mediumTurnPlayerService;
    private static CFDCalculatorService cfdCalculatorService;

    private TestGameContainer initialGameContainer;
    private Game turnPlayedGame;
    private Integer currentDayBefore;
    private Integer currentDayAfter;

    @BeforeClass
    public static void initService() {
        normalTurnPlayerService = Mockito.mock(NormalTurnPlayerService.class);
        mediumTurnPlayerService = Mockito.mock(MediumTurnPlayerService.class);
        cfdCalculatorService = Mockito.mock(CFDCalculatorService.class);
        Mockito.when(normalTurnPlayerService.playTurn(Mockito.any(Game.class), Mockito.any(Turn.class)))
                .then(returnsFirstArg());
        Mockito.when(cfdCalculatorService.calculateCFDForCurrentDay(Mockito.any(Game.class))).then(returnsFirstArg());
        playerService = new PlayerService(normalTurnPlayerService, mediumTurnPlayerService, cfdCalculatorService);
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

            @Test
            public void shouldCallTurnPlayerService() {
                Game game = initialGameContainer.getGame();
                Mockito.verify(normalTurnPlayerService, Mockito.times(1)).playTurn(game, validTurn);
                Mockito.verify(cfdCalculatorService, Mockito.times(1)).calculateCFDForCurrentDay(game);
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

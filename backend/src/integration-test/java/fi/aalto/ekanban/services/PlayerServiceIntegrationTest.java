package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.SpringIntegrationTest;
import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class PlayerServiceIntegrationTest extends SpringIntegrationTest {

    @Autowired
    PlayerService playerService;

    public class playTurn {
        private Game newGame;
        private final Integer CURRENT_DAY_BEFORE = 5;

        @Before
        public void setup() {
            TestGameContainer initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
            initialGameContainer.getGame().setCurrentDay(CURRENT_DAY_BEFORE);
            initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY_BEFORE);
            initialGameContainer.fillFirstWorkPhasesWithSomeAlmostReadyCards();
            initialGameContainer.fillLastWorkPhaseWithSomeAlmostReadyCards();
            Turn blankTurn = TurnBuilder.aTurn().build();
            newGame = playerService.playTurn(initialGameContainer.getGame(), blankTurn);
        }

        @Test
        public void shouldIncrementCurrentDay() {
            assertThat(newGame.getCurrentDay(), equalTo(CURRENT_DAY_BEFORE + 1));
        }

        @Test
        public void shouldAddCFDForTheCurrentDay() {
            assertThat(newGame.getCFD().getCfdDailyValues().size(), equalTo(newGame.getCurrentDay() + 1));
        }
    }
}

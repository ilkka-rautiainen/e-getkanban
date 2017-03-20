package fi.aalto.ekanban.models.db.games;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.builders.MoveCardActionBuilder;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class GameTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Game.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

    public class performMoveCardsAction {
        private TestGameContainer testGameContainer;
        private final Integer CURRENT_DAY = 7;

        @Before
        public void initGameAndBoard() {
            testGameContainer = TestGameContainer.withNormalDifficultyMockGame();
            testGameContainer.fillFirstWorkPhasesToFullWithReadyCards();
            testGameContainer.fillLastWorkPhaseWithSomeReadyCards();
            testGameContainer.getGame().setCurrentDay(CURRENT_DAY);
        }

        public class withCardMovingToDeployed {
            private Card cardBeforeInTest;
            private Card cardAfterInDeployed;

            @Before
            public void initAndDoAction() {
                cardBeforeInTest = testGameContainer.getTestPhase().getFirstColumn().getCards().get(0);
                MoveCardAction action = MoveCardActionBuilder.aMoveCardAction()
                        .withCardId(cardBeforeInTest.getId())
                        .withFromColumnId(testGameContainer.getTestPhase().getFirstColumn().getId())
                        .withToColumnId(testGameContainer.getDeployedPhase().getFirstColumn().getId())
                        .build();
                testGameContainer.getGame().performMoveCardAction(action);
                cardAfterInDeployed = testGameContainer.getDeployedPhase().getFirstColumn().getCards().get(0);
            }

            @Test
            public void shouldMoveCardToDeployed() {
                assertThat(cardAfterInDeployed.getId(), equalTo(cardBeforeInTest.getId()));
            }

            @Test
            public void shouldSetDayDeployedToTheCard() {
                assertThat(cardAfterInDeployed.getDayDeployed(), equalTo(CURRENT_DAY));
            }

            @Test
            public void shouldCalculateLeadTimeToTheCard() {
                assertThat(cardAfterInDeployed.getLeadTimeInDays(),
                        equalTo(CURRENT_DAY - cardAfterInDeployed.getDayStarted()));
            }
        }

        public class withCardNotMovingToDeployed {
            private Card cardBeforeInDevelopment;
            private Card cardAfterInTest;

            @Before
            public void initAndDoAction() {
                Integer indexForLastCardInDevelopment = testGameContainer.getDevelopmentPhase().getSecondColumn().getCards().size() - 1;
                cardBeforeInDevelopment = testGameContainer.getDevelopmentPhase().getSecondColumn().getCards()
                        .get(indexForLastCardInDevelopment);
                MoveCardAction action = MoveCardActionBuilder.aMoveCardAction()
                        .withCardId(cardBeforeInDevelopment.getId())
                        .withFromColumnId(testGameContainer.getDevelopmentPhase().getSecondColumn().getId())
                        .withToColumnId(testGameContainer.getTestPhase().getFirstColumn().getId())
                        .build();
                testGameContainer.getGame().performMoveCardAction(action);
                Integer indexForLastCardInTest = testGameContainer.getTestPhase().getFirstColumn().getCards().size() - 1;
                cardAfterInTest = testGameContainer.getTestPhase().getFirstColumn().getCards().get(indexForLastCardInTest);
            }

            @Test
            public void shouldMoveCardToTest() {
                assertThat(cardAfterInTest.getId(), equalTo(cardBeforeInDevelopment.getId()));
            }

            @Test
            public void shouldNotSetDayDeployed() {
                assertThat(cardAfterInTest.getDayDeployed(), is(nullValue()));
            }

            @Test
            public void shouldNotCalculateLeadTime() {
                assertThat(cardAfterInTest.getLeadTimeInDays(), is(nullValue()));
            }
        }
    }

}

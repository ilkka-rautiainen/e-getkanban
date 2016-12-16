package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import java.util.List;
import java.util.stream.Collectors;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import fi.aalto.ekanban.models.db.games.Card;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.utils.TestGameContainer;
import fi.aalto.ekanban.services.ai.movecards.MoveCardsAIService;

@RunWith(HierarchicalContextRunner.class)
public class MoveCardsAIServiceTest {
    private static MoveCardsAIService moveCardsAIService;
    private TestGameContainer initialGameContainer;
    private List<MoveCardAction> generatedActions;
    private List<String> toColumnIds;

    @BeforeClass
    public static void initService() {
        moveCardsAIService = new MoveCardsAIService();
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
    }

    public class withInProgressCards {
        @Before
        public void initAndDoAction() {
            initialGameContainer.fillFirstWorkPhasesWithSomeCardsInProgress();
            initialGameContainer.fillLastWorkPhaseWithSomeCardsInProgress();
            getMoveCardActions();
        }

        @Test
        public void shouldReturnEmptyActionList() {
            assertThat(generatedActions, is(notNullValue()));
            assertThat(generatedActions.size(), equalTo(0));
        }
    }

    public class withOneReadyCardInAllWorkColumns {
        @Before
        public void initAndDoAction() {
            initialGameContainer.fillFirstWorkPhasesWithSomeReadyCards();
            initialGameContainer.fillLastWorkPhaseWithSomeReadyCards();
            getMoveCardActions();
        }

        @Test
        public void shouldReturnFiveActions() {
            assertThat(generatedActions.size(), equalTo(5));
        }

        @Test
        public void shouldMoveOneCardToDeployed() {
            shouldMoveOneCardTo(initialGameContainer.getDeployedPhase());
        }

        @Test
        public void shouldMoveOneCardToTest() {
            shouldMoveOneCardTo(initialGameContainer.getTestPhase());
        }

        @Test
        public void shouldMoveOneCardToDevelopment() {
            shouldMoveOneCardTo(initialGameContainer.getDevelopmentPhase());
        }

        private void shouldMoveOneCardTo(Phase phase) {
            String toColumnId = phase.getFirstColumn().getId();
            assertThat(generatedActions, hasItem(hasProperty("toColumnId", is(toColumnId))));
        }
    }

    public class withReadyCardsAllColumnsFull {
        private List<Card> cardsInTesting;
        private String testColumnId;

        @Before
        public void initAndDoAction() {
            initialGameContainer.fillFirstWorkPhasesToFullWithReadyCards();
            initialGameContainer.fillLastWorkPhaseToFullWithReadyCards();
            cardsInTesting = initialGameContainer.getTestPhase().getFirstColumn().getCards();
            testColumnId = initialGameContainer.getTestPhase().getFirstColumn().getId();
            getMoveCardActions();
        }

        @Test
        public void shouldEmptyTestingFromItsInitialCards() {
            String deployedColumnId = initialGameContainer.getDeployedPhase().getFirstColumn().getId();
            cardsInTesting.forEach(cardInTesting -> {
                MoveCardAction moveCardAction = generatedActions.stream()
                        .filter(action -> action.getCardId().equals(cardInTesting.getId()))
                        .filter(action -> action.getFromColumnId().equals(testColumnId))
                        .findFirst().orElse(null);
                assertThat(moveCardAction, is(notNullValue()));
                assertThat(moveCardAction.getToColumnId(), equalTo(deployedColumnId));
            });
        }

        @Test
        public void shouldFillTestingWithCardsFromDevelopmenReady() {
            String developmentReadyColumn = initialGameContainer.getDevelopmentPhase().getSecondColumn().getId();
            Integer cardsFromDevelopmentReady = (int) generatedActions.stream()
                    .filter(action -> action.getToColumnId().equals(testColumnId))
                    .filter(action -> action.getFromColumnId().equals(developmentReadyColumn))
                    .count();

            Integer cardsFromDevReady = 2;
            Integer cardsFromDevInProgressThatWentThroughDevReady = 1;
            Integer allCardsFromDevReady = cardsFromDevReady + cardsFromDevInProgressThatWentThroughDevReady;
            assertThat(cardsFromDevelopmentReady, equalTo(allCardsFromDevReady));
        }
    }

    private void getMoveCardActions() {
        generatedActions = moveCardsAIService.getMoveCardsActions(initialGameContainer.getGame());
        toColumnIds = generatedActions.stream().map(MoveCardAction::getToColumnId).collect(Collectors.toList());
    }
}

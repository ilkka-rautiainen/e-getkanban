package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.TEST_PHASE_ID;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.DiceCastAction;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.services.ai.assignresources.AssignResourcesAIService;
import fi.aalto.ekanban.services.ai.assignresources.DiceCastService;
import fi.aalto.ekanban.services.dice.DiceService;
import fi.aalto.ekanban.services.dice.DiceServiceMock;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class AssignResourcesAIServiceIntegrationTest {

    private static AssignResourcesAIService assignResourcesAIService;
    private static DiceCastService diceCastService;

    private TestGameContainer initialGameContainer;
    private List<AssignResourcesAction> generatedActions;

    @BeforeClass
    public static void initService() {
        assignResourcesAIService = new AssignResourcesAIService();
        DiceService diceServiceMock = new DiceServiceMock();
        diceCastService = new DiceCastService(diceServiceMock);
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
    }

    public class withoutActionsExecuted {

        public class withReadyCards {
            @Before
            public void initAndDoAction() {
                initialGameContainer.fillFirstWorkPhasesToFullWithReadyCards();
                initialGameContainer.fillLastWorkPhaseToFullWithReadyCards();
                getAssignResourcesActions();
            }

            @Test
            public void shouldNotReturnAnyActionsBecauseCardsAreReady() {
                assertThat(generatedActions.size(), equalTo(0));
            }
        }

        public class withAlmostReadyCards {
            private Map<String, List<Card>> cardsByPhaseId;

            @Before
            public void initAndDoAction() {
                initialGameContainer.fillFirstWorkPhasesToFullWithAlmostReadyCards();
                initialGameContainer.fillLastWorkPhaseToFullWithAlmostReadyCards();
                getAssignResourcesActions();
                cardsByPhaseId = initialGameContainer.getGame().getBoard().getWorkPhases().stream()
                        .collect(Collectors.toMap(Phase::getId, Phase::getAllCards));
            }

            @Test
            public void shouldFillAllCards() {
                for (Map.Entry<String, List<Card>> entry : cardsByPhaseId.entrySet()) {
                    for (Card card : entry.getValue()) {
                        String phaseId = entry.getKey();
                        assertPointsFilledInCardForPhase(card, phaseId);
                    }
                }
            }
        }

        public class withInProgressCards {
            private Map<String, List<Card>> cardsByPhaseId;

            @Before
            public void initAndDoAction() {
                initialGameContainer.fillFirstWorkPhasesToFullWithCardsInProgress();
                initialGameContainer.fillLastWorkPhaseToFullWithCardsInProgress();
                getAssignResourcesActions();
                cardsByPhaseId = initialGameContainer.getGame().getBoard().getWorkPhases().stream()
                        .collect(Collectors.toMap(Phase::getId, Phase::getAllCards));
            }

            /*
             * analysis has 2 cards, each of them 5 points left -> 10 points left
             * dice value mocked to 5 and analysis has 2 dice -> 10 points generated
             */
            @Test
            public void shouldFillAllCardsInAnalysis() {
                List<Card> analysisCards = cardsByPhaseId.get(ANALYSIS_PHASE_ID);
                for (Card card : analysisCards) {
                    assertPointsFilledInCardForPhase(card, ANALYSIS_PHASE_ID);
                }
            }

            /*
             * development has 4 cards, each of them 5 points left -> 20 points left
             * dice value mocked to 5 and development has 3 dice -> 15 points generated
             */
            @Test
            public void shouldPartlyFillCardsInDevelopment() {
                List<Card> developmentCards = cardsByPhaseId.get(DEVELOPMENT_PHASE_ID);
                shouldFillCard(developmentCards.get(0), DEVELOPMENT_PHASE_ID);
                shouldFillCard(developmentCards.get(1), DEVELOPMENT_PHASE_ID);
                shouldFillCard(developmentCards.get(2), DEVELOPMENT_PHASE_ID);
                shouldNotFillCardAtAll(developmentCards.get(3), DEVELOPMENT_PHASE_ID);
            }

            /*
             * test-phase has 3 cards, each of them 5 points left -> 15 points left
             * dice value mocked to 5 and test-phase has 2 dice -> 10 points generated
             */
            @Test
            public void shouldPartlyFillCardsInTest() {
                List<Card> testCards = cardsByPhaseId.get(TEST_PHASE_ID);
                shouldFillCard(testCards.get(0), TEST_PHASE_ID);
                shouldFillCard(testCards.get(1), TEST_PHASE_ID);
                shouldNotFillCardAtAll(testCards.get(2), TEST_PHASE_ID);
            }

            private void shouldFillCard(Card card, String phaseId) {
                AssignResourcesAction action = getActionForCardInPhase(card, phaseId);
                assertThat(action.getPoints(), equalTo(card.getCardPhasePointOfPhase(phaseId).pointsUntilDone()));
            }

            private void shouldNotFillCardAtAll(Card card, String phaseId) {
                AssignResourcesAction action = getActionForCardInPhase(card, phaseId);
                assertThat(action, is(nullValue()));
            }
        }

        private void assertPointsFilledInCardForPhase(Card card, String phaseId) {
            CardPhasePoint cardPhasePointOfPhase = card.getCardPhasePointOfPhase(phaseId);
            AssignResourcesAction action = getActionForCardInPhase(card, phaseId);
            assertThat(action, is(notNullValue()));
            assertThat(action.getPoints(), equalTo(cardPhasePointOfPhase.pointsUntilDone()));
        }

        private AssignResourcesAction getActionForCardInPhase(Card card, String phaseId) {
            return generatedActions.stream()
                    .filter(a -> a.getCardId().equals(card.getId()))
                    .filter(a -> a.getPhaseId().equals(phaseId))
                    .findFirst().orElse(null);
        }

        private void getAssignResourcesActions() {
            List<DiceCastAction> diceCastActions = diceCastService.getDiceCastActions(initialGameContainer.getGame());
            generatedActions = assignResourcesAIService.getAssignResourcesActions(initialGameContainer.getGame(),
                    diceCastActions);
        }
    }

    public class withActionsExecuted {
        private TestGameContainer resourcesUsedGameContainer;

        public class withAllPhasesFullOfAlmostReadyCards {
            @Before
            public void initAndDoAction() {
                initialGameContainer.fillFirstWorkPhasesToFullWithAlmostReadyCards();
                initialGameContainer.fillLastWorkPhaseToFullWithAlmostReadyCards();
                performAssignResources();
            }

            @Test
            public void shouldFillAllCards() {
                List<Phase> workPhases = resourcesUsedGameContainer.getGame().getBoard().getWorkPhases();
                for (Phase phase : workPhases) {
                    List<Card> firstColumnCards = phase.getFirstColumn().getCards();
                    for (Card card : firstColumnCards) {
                        CardPhasePoint phasePoint = card.getCardPhasePointOfPhase(phase.getId());
                        assertThat(phasePoint.isReady(), equalTo(true));
                    }
                }
            }
        }

        private void performAssignResources() {
            List<DiceCastAction> diceCastActions = diceCastService.getDiceCastActions(initialGameContainer.getGame());
            List<AssignResourcesAction> actions = assignResourcesAIService
                    .getAssignResourcesActions(initialGameContainer.getGame(), diceCastActions);
            ActionExecutorService actionExecutorService = new ActionExecutorService();
            Game gameWithResourcesUsed = actionExecutorService.assignResources(initialGameContainer.getGame(), actions);
            resourcesUsedGameContainer = TestGameContainer.withGame(gameWithResourcesUsed);
        }
    }
}

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
import org.mockito.Mockito;

import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.services.AssignResourcesAI.AssignResourcesAIService;
import fi.aalto.ekanban.services.AssignResourcesAI.DiceService;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class AssignResourcesAIServiceTest {

    private static final Integer DICE_VALUE = 10;

    private static AssignResourcesAIService assignResourcesAIService;

    private TestGameContainer initialGameContainer;
    private List<AssignResourcesAction> generatedActions;

    @BeforeClass
    public static void initService() {
        DiceService diceService = Mockito.mock(DiceService.class);
        Mockito.when(diceService.cast(Mockito.anyInt(), Mockito.anyInt())).thenReturn(DICE_VALUE);
        assignResourcesAIService = new AssignResourcesAIService(diceService);
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
    }

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
         * dice value mocked to 10 and analysis has 1 dice -> 10 points generated
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
         * dice value mocked to 10 and development has 2 dice -> 20 points generated
         */
        @Test
        public void shouldFillAllCardsInDevelopment() {
            List<Card> developmentCards = cardsByPhaseId.get(DEVELOPMENT_PHASE_ID);
            for (Card card : developmentCards) {
                assertPointsFilledInCardForPhase(card, DEVELOPMENT_PHASE_ID);
            }
        }

        /*
         * test-phase has 3 cards, each of them 5 points left -> 15 points left
         * dice value mocked to 10 and test-phase has 1 dice -> 10 points generated
         */
        @Test
        public void shouldPartlyFillCardsInTest() {
            List<Card> testCards = cardsByPhaseId.get(TEST_PHASE_ID);
            shouldFillCardInTest(testCards.get(2));
            shouldFillCardInTest(testCards.get(1));
            shouldNotFillCardInTestAtAll(testCards.get(0));
        }

        private void shouldFillCardInTest(Card card) {
            AssignResourcesAction action = getActionForCardInPhase(card, TEST_PHASE_ID);
            assertThat(action.getPoints(), equalTo(card.getCardPhasePointOfPhase(TEST_PHASE_ID).pointsUntilDone()));
        }

        private void shouldNotFillCardInTestAtAll(Card card) {
            AssignResourcesAction action = getActionForCardInPhase(card, TEST_PHASE_ID);
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
        generatedActions = assignResourcesAIService.getAssignResourcesActions(initialGameContainer.getGame());
    }
}

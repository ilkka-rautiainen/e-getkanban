package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.services.AssignResourcesAI.AssignResourcesAIService;
import fi.aalto.ekanban.services.AssignResourcesAI.DiceService;
import fi.aalto.ekanban.utils.TestGameContainer;


@RunWith(HierarchicalContextRunner.class)
public class AssignResourcesAIIntegrationTest {

    private static AssignResourcesAIService assignResourcesAIService;

    private TestGameContainer initialGameContainer;
    private TestGameContainer resourcesUsedGameContainer;

    @BeforeClass
    public static void initService() {
        DiceService diceService = new DiceService();
        assignResourcesAIService = new AssignResourcesAIService(diceService);
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
    }

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
        List<AssignResourcesAction> actions = assignResourcesAIService
                .getAssignResourcesActions(initialGameContainer.getGame());
        Game gameWithResourcesUsed = PlayerService.assignResources(initialGameContainer.getGame(), actions);
        resourcesUsedGameContainer = TestGameContainer.withGame(gameWithResourcesUsed);
    }
}

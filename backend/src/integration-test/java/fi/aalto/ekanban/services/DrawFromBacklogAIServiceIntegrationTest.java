package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;

import com.rits.cloning.Cloner;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.models.DrawFromBacklogAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.utils.TestGameContainer;
import fi.aalto.ekanban.services.ai.drawfrombacklog.DrawFromBacklogAIService;

@RunWith(HierarchicalContextRunner.class)
public class DrawFromBacklogAIServiceIntegrationTest {

    private static DrawFromBacklogAIService drawFromBacklogAIService;

    private TestGameContainer initialGameContainer;
    private TestGameContainer drawnCardsGameContainer;

    private Integer cardsInBacklogAfter;
    private Integer cardsInFirstPhaseAfter;

    @BeforeClass
    public static void setUpMoveCardsAIService() {
        drawFromBacklogAIService = new DrawFromBacklogAIService();
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
        initialGameContainer.getGame().getBoard().getPhases().forEach(phase -> phase.setWipLimit(5));
    }

    public class withFirstPhaseFull {
        private Integer cardsInBacklogBefore;
        private Integer cardsInFirstPhaseBefore;

        @Before
        public void initAndDoAction() {
            initialGameContainer.fillFirstWorkPhasesToFullWithReadyCards();

            cardsInBacklogBefore = initialGameContainer.getGame().getBoard().getBacklogDeck().size();
            cardsInFirstPhaseBefore = initialGameContainer.getAnalysisPhase().getTotalAmountOfCards();

            performDrawFromBacklog();
        }

        @Test
        public void shouldRemainBacklogDeckIntact() {
            assertThat(cardsInBacklogAfter, equalTo(cardsInBacklogBefore));
        }

        @Test
        public void shouldRemainFirstPhaseIntact() {
            assertThat(cardsInFirstPhaseAfter, equalTo(cardsInFirstPhaseBefore));
        }
    }

    public class withEmptyFirstPhase {
        private Integer cardsInBacklogBefore;
        private Integer cardsInFirstPhaseBefore;
        private Integer placesInFirstPhaseBefore;
        private List<Card> firstCardsInBacklogBefore;

        @Before
        public void initAndDoAction() {
            List<Card> backlogDeck = initialGameContainer.getGame().getBoard().getBacklogDeck();

            cardsInBacklogBefore = backlogDeck.size();
            cardsInFirstPhaseBefore = initialGameContainer.getAnalysisPhase().getTotalAmountOfCards();
            placesInFirstPhaseBefore = initialGameContainer.getAnalysisPhase().getWipLimit() -
                    initialGameContainer.getAnalysisPhase().getTotalAmountOfCards();
            firstCardsInBacklogBefore = new Cloner().deepClone(backlogDeck.subList(0, placesInFirstPhaseBefore));

            performDrawFromBacklog();
        }

        @Test
        public void shouldFillFirstPhaseToFull() {
            assertThat(drawnCardsGameContainer.getAnalysisPhase().isFullWip(), equalTo(true));
        }

        @Test
        public void shouldRemoveRightAmountOfCardsFromBacklog() {
            Integer cardsRemovedFromBacklog = cardsInBacklogBefore - cardsInBacklogAfter;
            assertThat(cardsRemovedFromBacklog, equalTo(placesInFirstPhaseBefore));
        }

        @Test
        public void shouldAddRightAmountOfCardsToFirstPhase() {
            Integer cardsAddedToFirstPhase = cardsInFirstPhaseAfter - cardsInFirstPhaseBefore;
            assertThat(cardsAddedToFirstPhase, equalTo(placesInFirstPhaseBefore));
        }

        @Test
        public void shouldAddTheCardsInRightOrder() {
            List<Card> cardsInFirstColumn = drawnCardsGameContainer.getAnalysisPhase().getFirstColumn().getCards();

            for (Integer cardIndex = 0; cardIndex < cardsInFirstColumn.size(); cardIndex++) {
                Card cardToRemoveFromBacklog = firstCardsInBacklogBefore.get(cardIndex);
                Card cardInFirstColumn = cardsInFirstColumn.get(cardIndex);
                assertThat(cardToRemoveFromBacklog.getOrderNumber(), equalTo(cardInFirstColumn.getOrderNumber()));
            }
        }

    }

    public class withRestrictedBacklogDeck {
        private Integer cardsInBacklogBefore;
        private Integer cardsInFirstPhaseBefore;

        @Before
        public void initAndDoAction() {
            initialGameContainer.fillFirstWorkPhasesWithSomeReadyCards();
            initialGameContainer.removeAllButOneCardFromBacklog();

            cardsInBacklogBefore = initialGameContainer.getGame().getBoard().getBacklogDeck().size();
            cardsInFirstPhaseBefore = initialGameContainer.getAnalysisPhase().getTotalAmountOfCards();

            performDrawFromBacklog();
        }

        @Test
        public void shouldRemoveOneCardFromBacklog() {
            Integer cardsRemovedFromBacklog = cardsInBacklogBefore - cardsInBacklogAfter;
            assertThat(cardsRemovedFromBacklog, equalTo(1));
        }

        @Test
        public void shouldAddOneCardToFirstPhase() {
            Integer cardsAddedToFirstPhase = cardsInFirstPhaseAfter - cardsInFirstPhaseBefore;
            assertThat(cardsAddedToFirstPhase, equalTo(1));
        }

        @Test
        public void shouldLeaveBacklogDeckEmpty() {
            assertThat(cardsInBacklogAfter, equalTo(0));
        }
    }

    private void performDrawFromBacklog() {
        List<DrawFromBacklogAction> actions = drawFromBacklogAIService
                .getDrawFromBacklogActions(initialGameContainer.getGame());
        ActionExecutorService actionExecutorService = new ActionExecutorService();
        Game gameWithCardsDrawn = actionExecutorService.drawFromBacklog(initialGameContainer.getGame(), actions);
        drawnCardsGameContainer = TestGameContainer.withGame(gameWithCardsDrawn);
        setResults();
    }

    private void setResults() {
        cardsInBacklogAfter = drawnCardsGameContainer.getGame().getBoard().getBacklogDeck().size();
        cardsInFirstPhaseAfter = drawnCardsGameContainer.getAnalysisPhase().getTotalAmountOfCards();
    }

}

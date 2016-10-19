package fi.aalto.ekanban.services;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static fi.aalto.ekanban.services.PlayerService.adjustWipLimits;
import static fi.aalto.ekanban.services.PlayerService.drawFromBacklog;

import java.util.*;

import com.rits.cloning.Cloner;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import fi.aalto.ekanban.builders.CardBuilder;
import fi.aalto.ekanban.builders.DrawFromBacklogActionBuilder;
import fi.aalto.ekanban.models.DrawFromBacklogAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.phases.Phase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.builders.AdjustWipLimitsActionBuilder;
import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.db.games.Game;

@RunWith(HierarchicalContextRunner.class)
public class PlayerServiceTest {

    private static PlayerService playerService;

    private TestGameContainer initialGameContainer;

    @BeforeClass
    public static void setUpPlayerService() {
        playerService = new PlayerService();
    }

    @Before
    public void initGame() {
        initialGameContainer = TestGameContainer.withNormalDifficultyGame();
    }

    public class adjustWipLimits {

        private Game wipLimitAdjustedGame;
        private Map<String, Integer> phaseWipLimits;
        private Random random;

        @Before
        public void initGame() {
            phaseWipLimits = new HashMap<>();
            random = new Random();
        }

        @After
        public void emptyPhaseWipLimits() {
            phaseWipLimits.clear();
        }

        public class withNoWipLimitChangesGiven {

            private int[] originalWipLimits;

            @Before
            public void doAction() {
                originalWipLimits = initialGameContainer.getGame().getBoard().getPhases().stream()
                        .filter(phase -> phase.getWipLimit() != null)
                        .mapToInt(phase -> phase.getWipLimit()).toArray();

                AdjustWipLimitsAction emptyWipLimitChangeAction = null;
                wipLimitAdjustedGame = adjustWipLimits(initialGameContainer.getGame(), emptyWipLimitChangeAction);
            }

            @Test
            public void shouldReturnIdenticalWipLimitsToOriginal() {
                for (Integer i = 0; i < originalWipLimits.length; i++) {
                    Integer originalWipLimitForPhase = originalWipLimits[i];
                    Integer newWipLimitForPhase = wipLimitAdjustedGame.getBoard().getPhases().get(i).getWipLimit();
                    assertThat(newWipLimitForPhase, equalTo(originalWipLimitForPhase));
                }
            }
        }

        public class withSinglePhaseWipLimitChanged {
            private String phaseId;
            Integer randomlySelectedPhase;
            Integer newWipLimit;

            @Before
            public void doAction() {
                randomlySelectedPhase = random.nextInt(4);
                phaseId = initialGameContainer.getGame().getBoard().getPhases().get(randomlySelectedPhase).getId();
                newWipLimit = 5;
                phaseWipLimits.put(phaseId, newWipLimit);
                AdjustWipLimitsAction singlePhaseWipLimitChangeAction =
                        AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction()
                                .withPhaseWipLimits(phaseWipLimits)
                                .build();
                wipLimitAdjustedGame = adjustWipLimits(initialGameContainer.getGame(), singlePhaseWipLimitChangeAction);
            }

            @Test
            public void shouldSetPhaseWipLimitToGivenValue() {
                Integer changedWipLimitOfPhase =
                        wipLimitAdjustedGame.getBoard().getPhases().get(randomlySelectedPhase).getWipLimit();
                assertThat(changedWipLimitOfPhase, equalTo(newWipLimit));
            }

        }

        public class withMultiplePhaseWipLimitsChanged {
            Integer newWipLimitOfAnalysisPhase = 0;
            Integer newWipLimitOfDevelopmentPhase = 5;
            Integer newWipLimitOfTestPhase = 6;

            @Before
            public void doAction() {
                Game initialGame = initialGameContainer.getGame();
                String analysisPhaseId = initialGame.getBoard().getPhases().get(0).getId();
                String developmentPhaseId = initialGame.getBoard().getPhases().get(1).getId();
                String testPhaseId = initialGame.getBoard().getPhases().get(2).getId();
                phaseWipLimits.put(analysisPhaseId, newWipLimitOfAnalysisPhase);
                phaseWipLimits.put(developmentPhaseId, newWipLimitOfDevelopmentPhase);
                phaseWipLimits.put(testPhaseId, newWipLimitOfTestPhase);

                AdjustWipLimitsAction singlePhaseWipLimitChangeAction =
                        AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction()
                                .withPhaseWipLimits(phaseWipLimits)
                                .build();
                wipLimitAdjustedGame = adjustWipLimits(initialGame, singlePhaseWipLimitChangeAction);
            }

            @Test
            public void shouldSetAllWipLimitsToNewGivenValues() {
                Integer changedWipLimitOfAnalysisPhase =
                        wipLimitAdjustedGame.getBoard().getPhases().get(0).getWipLimit();
                Integer changedWipLimitOfDevelopmentPhase =
                        wipLimitAdjustedGame.getBoard().getPhases().get(1).getWipLimit();
                Integer changedWipLimitOfTestPhase =
                        wipLimitAdjustedGame.getBoard().getPhases().get(2).getWipLimit();
                assertThat(changedWipLimitOfAnalysisPhase, equalTo(newWipLimitOfAnalysisPhase));
                assertThat(changedWipLimitOfDevelopmentPhase, equalTo(newWipLimitOfDevelopmentPhase));
                assertThat(changedWipLimitOfTestPhase, equalTo(newWipLimitOfTestPhase));
            }

        }

    }

    public class drawFromBacklog {

        private TestGameContainer gameWithBacklogDrawnContainer;
        private Card cardToBeDrawnFromBacklogDeck;
        private List<Card> firstColumnCards;

        @Before
        public void initContext() {
            cardToBeDrawnFromBacklogDeck = initialGameContainer.getGame().getBoard().getBacklogDeck().get(0);
            Phase firstPhase = initialGameContainer.getGame().getBoard().getPhases().get(0);
            firstPhase.setWipLimit(2);
            firstColumnCards = initialGameContainer.getGame().getBoard()
                    .getPhases().get(0).getColumns().get(0).getCards();
        }

        private void drawCardToIndex(Integer index) {
            DrawFromBacklogAction backlogAction = DrawFromBacklogActionBuilder
                    .aDrawFromBacklogAction()
                    .withIndexToPlaceCardAt(index)
                    .build();
            Game gameWithBacklogDrawn = drawFromBacklog(initialGameContainer.getGame(), Arrays.asList(backlogAction));
            gameWithBacklogDrawnContainer = TestGameContainer.withGame(gameWithBacklogDrawn);
            firstColumnCards = gameWithBacklogDrawnContainer.getGame().getBoard()
                    .getPhases().get(0).getColumns().get(0).getCards();
        }

        public class whenWipLimitIsNotFull {
            @Before
            public void setOneCardToFirstColumn() {
                firstColumnCards.add(CardBuilder.aCard().build());
            }

            @Test
            public void shouldSetDrawnCardToFirstColumn() {
                Integer countOfFirstColumnCardsBeforeBacklogDraw = firstColumnCards.size();
                drawCardToIndex(0);
                Integer countOfFirstColumnCardsAfterBacklogDraw = firstColumnCards.size();

                assertThat(countOfFirstColumnCardsAfterBacklogDraw,
                        equalTo(countOfFirstColumnCardsBeforeBacklogDraw + 1));
            }

            @Test
            public void shouldSetDrawnCardToFirstPosition() {
                drawCardToIndex(0);
                Card firstCardOnTheFirstColumn = firstColumnCards.get(0);

                assertThat(cardToBeDrawnFromBacklogDeck, equalTo(firstCardOnTheFirstColumn));
                assertThat(firstColumnCards.size(), equalTo(2));
            }

            @Test
            public void shouldSetDrawnCardBetweenFirstAndLastPosition() {
                initialGameContainer.getGame().getBoard().getPhases().get(0).setWipLimit(3);
                firstColumnCards.add(CardBuilder.aCard().build());
                drawCardToIndex(1);

                Card secondCardOnTheFirstColumn = firstColumnCards.get(1);
                assertThat(cardToBeDrawnFromBacklogDeck, equalTo(secondCardOnTheFirstColumn));
                assertThat(firstColumnCards.size(), equalTo(3));
            }

            @Test
            public void shouldSetDrawnCardToLastPosition() {
                Integer lastPosition = firstColumnCards.size();
                drawCardToIndex(lastPosition);

                Card lastCardOnTheFirstColumn = firstColumnCards.get(firstColumnCards.size() - 1);
                assertThat(cardToBeDrawnFromBacklogDeck, equalTo(lastCardOnTheFirstColumn));
                assertThat(firstColumnCards.size(), equalTo(2));
            }

            @Test
            public void shouldChangeBacklogDeckCardCountByOne() {
                Integer backlogDeckCardCountBeforeAction = initialGameContainer.getGame().getBoard()
                        .getBacklogDeck().size();
                drawCardToIndex(0);
                Integer backlogDeckCardCountAfterAction = gameWithBacklogDrawnContainer.getGame().getBoard()
                        .getBacklogDeck().size();

                assertThat(backlogDeckCardCountAfterAction, equalTo(backlogDeckCardCountBeforeAction - 1));
            }

            @Test
            public void shouldRemoveTheFirstCardFromBacklogDeck() {
                Card firstCardOnBacklogDeckBeforeAction = initialGameContainer.getGame().getBoard()
                        .getBacklogDeck().get(0);
                drawCardToIndex(0);
                Card firstCardOnBacklogDeckAfterAction = gameWithBacklogDrawnContainer.getGame().getBoard()
                        .getBacklogDeck().get(0);

                assertThat(firstCardOnBacklogDeckBeforeAction, not(equalTo(firstCardOnBacklogDeckAfterAction)));
            }

            @Test
            public void shouldSetDayStartedToCurrentDay() {
                drawCardToIndex(0);
                assertThat(cardToBeDrawnFromBacklogDeck.getDayStarted(),
                        equalTo(initialGameContainer.getGame().getCurrentDay()));
            }
        }

        public class whenWipLimitIsFull {

            @Before
            public void setFirstPhaseToFullWipLimit() {
                firstColumnCards.add(CardBuilder.aCard().build());
                firstColumnCards.add(CardBuilder.aCard().build());
            }

            @Test
            public void shouldNotSetCardToColumn() {
                assertShouldNotSedCardToColumnWhenDrawnTo(0);
            }

            @Test
            public void shouldNotChangeBacklogDeckCardCount() {
                assertShouldChangeBacklogDeckCardCountWhenDrawnTo(0);
            }

        }

        public class whenInvalidIndexIsGiven {
            @Test
            public void shouldNotSetCardToColumn() {
                assertShouldNotSedCardToColumnWhenDrawnTo(1);
            }

            @Test
            public void shouldNotChangeBacklogDeckCardCount() {
                assertShouldChangeBacklogDeckCardCountWhenDrawnTo(1);
            }
        }

        private void assertShouldChangeBacklogDeckCardCountWhenDrawnTo(Integer index) {
            Integer backlogDeckCardCountBeforeAction = initialGameContainer.getGame().getBoard()
                    .getBacklogDeck().size();
            drawCardToIndex(index);
            Integer backlogDeckCardCountAfterAction = gameWithBacklogDrawnContainer.getGame().getBoard()
                    .getBacklogDeck().size();

            assertThat(backlogDeckCardCountAfterAction, equalTo(backlogDeckCardCountBeforeAction));
        }

        private void assertShouldNotSedCardToColumnWhenDrawnTo(Integer index) {
            Cloner cloner = new Cloner();
            List<Card> cardsInFirstColumnBeforeAction = cloner.deepClone(firstColumnCards);
            Integer countOfCardsInFirstColumnBeforeBacklogDraw = firstColumnCards.size();
            drawCardToIndex(index);
            Integer countOfCardsInFirstColumnAfterBacklogDraw = firstColumnCards.size();

            assertThat(countOfCardsInFirstColumnAfterBacklogDraw,
                    equalTo(countOfCardsInFirstColumnBeforeBacklogDraw));
            for (Integer i = 0; i < firstColumnCards.size(); i++) {
                assertThat(cardsInFirstColumnBeforeAction.get(i), equalTo(firstColumnCards.get(i)));
            }
        }
    }
}

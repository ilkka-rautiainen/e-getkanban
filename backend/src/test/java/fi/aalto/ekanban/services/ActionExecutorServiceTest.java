package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;

import java.util.*;

import com.rits.cloning.Cloner;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import fi.aalto.ekanban.exceptions.ColumnNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.DrawFromBacklogAction;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class ActionExecutorServiceTest {

    private ActionExecutorService actionExecutorService;
    private TestGameContainer initialGameContainer;

    @Before
    public void init() {
        actionExecutorService = new ActionExecutorService();
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
    }

    public class moveCards {
        private Card firstCardToMove;
        private Card secondCardToMove;

        private TestGameContainer gameWithCardsMovedContainer;
        private Column fromColumnAfter;
        private Column toColumnAfter;

        @Before
        public void buildCardsToMoveAndSetColumns() {
            firstCardToMove = CardBuilder.aCard().withId("1").withDayStarted(1).build();
            secondCardToMove = CardBuilder.aCard().withId("2").withDayStarted(1).build();
        }

        public class withToColumnInSamePhase {

            public class andToColumnIsNextAdjacent {
                @Before
                public void moveCards() {
                    TestGameContainer.ColumnName fromColumnName = TestGameContainer.ColumnName.ANALYSIS_IN_PROGRESS;
                    TestGameContainer.ColumnName toColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                    moveTestGameCards(fromColumnName, toColumnName);
                }
                @Test
                public void shouldRemoveCardsFromOldColumn() {
                    assertThat(fromColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                }
                @Test
                public void shouldAllowAllCardsToBeMovedToNewColumn() {
                    assertThat(toColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                }
            }

            public class andToColumnIsPreviousAdjacent {
                @Before
                public void moveCards() {
                    TestGameContainer.ColumnName fromColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                    TestGameContainer.ColumnName toColumnName = TestGameContainer.ColumnName.ANALYSIS_IN_PROGRESS;
                    moveTestGameCards(fromColumnName, toColumnName);
                }
                @Test
                public void shouldRemainCardsInOldColumnIntact() {
                    assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                }
                @Test
                public void shouldNotMoveAnyCardsToNewColumn() {
                    assertThat(toColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                }
            }

            public class andTheColumnsAreTheSame {
                @Before
                public void moveCards() {
                    TestGameContainer.ColumnName fromColumnName = TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS;
                    TestGameContainer.ColumnName toColumnName = TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS;
                    moveTestGameCards(fromColumnName, toColumnName);
                }

                @Test
                public void shouldRemainCardsInOldColumnIntact() {
                    assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                }
            }
        }

        public class withToColumnInAnotherPhase {
            public class whenPhaseIsAdjacentNextOne {
                public class andToColumnIsNextAdjacent {
                    private TestGameContainer.ColumnName fromColumnName;
                    private TestGameContainer.ColumnName toColumnName;

                    public class andCardDeployCycleIsEnabled {
                        @Before
                        public void setFromAndToColumn() {
                            initialGameContainer = TestGameContainer.withAdvancedDifficultyMockGame();
                            fromColumnName = TestGameContainer.ColumnName.TEST;
                            toColumnName = TestGameContainer.ColumnName.DEPLOYED;
                        }

                        public class whenGameDayIsValidToDeploy {
                            @Before
                            public void moveCards() {
                                initialGameContainer.getGame().setCurrentDay(4);
                                moveTestGameCards(fromColumnName, toColumnName);
                            }
                            @Test
                            public void shouldRemoveCardsFromOldColumn() {
                                assertThat(fromColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                            }
                            @Test
                            public void shouldAllowAllCardsToBeMovedToNewColumn() {
                                assertThat(toColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                            }
                        }

                        public class whenGameDayIsNotReadyToDeploy {
                            @Before
                            public void moveCards() {
                                initialGameContainer.getGame().setCurrentDay(3);
                                moveTestGameCards(fromColumnName, toColumnName);
                            }
                            @Test
                            public void shouldRemainCardsInOldColumnIntact() {
                                assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                            }
                            @Test
                            public void shouldNotMoveAnyCardsToNewColumn() {
                                assertThat(toColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                            }
                        }
                    }

                    @Before
                    public void setFromAndToColumn() {
                        fromColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                        toColumnName = TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS;
                    }

                    public class andPhaseHoldsLessCardsThanWipLimitAllows {
                        public class whenWipLimitDoesNotGetFull {
                            @Before
                            public void moveCards() {
                                moveTestGameCards(fromColumnName, toColumnName);
                            }
                            @Test
                            public void shouldRemoveCardsFromOldColumn() {
                                assertThat(fromColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                            }
                            @Test
                            public void shouldAllowAllCardsToBeMovedToNewColumn() {
                                assertThat(toColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                            }
                        }

                        public class whenWipLimitGetsFull {
                            private Card fromColumnFirstCardBefore;
                            private Integer fromPhaseCardCountBefore;

                            @Before
                            public void fillToPhaseToOneToFullAndMoveCards() {
                                initialGameContainer.fillDevelopmentInProgressToOneToFullWip();

                                Column fromColumn = initialGameContainer.getColumn(fromColumnName);
                                addMovingCardsIntoFromColumn(fromColumn);
                                fromColumnFirstCardBefore = fromColumn.getCards().get(0);

                                Phase fromColumnPhase = initialGameContainer.getAnalysisPhase();
                                fromPhaseCardCountBefore = fromColumnPhase.getTotalAmountOfCards();

                                moveTestGameCards(fromColumnName, toColumnName);
                            }
                            @Test
                            public void shouldAllowOnlyOneCardToBeMovedToNewColumn() {
                                Phase afterMoveFromColumnPhase = gameWithCardsMovedContainer.getAnalysisPhase();
                                Integer amountOfRemovedCardsInFromPhase =
                                        fromPhaseCardCountBefore - afterMoveFromColumnPhase.getTotalAmountOfCards();
                                assertThat(amountOfRemovedCardsInFromPhase, equalTo(1));
                            }
                            @Test
                            public void shouldFillTheCardsOfNewPhaseUpToWipLimit() {
                                Phase afterMoveToColumnPhase = gameWithCardsMovedContainer.getDevelopmentPhase();
                                assertThat(afterMoveToColumnPhase.getWipLimit(),
                                        equalTo(afterMoveToColumnPhase.getTotalAmountOfCards()));
                            }
                            @Test
                            public void shouldLeaveSomeOfTheCardsToOldColumn() {
                                assertThat(fromColumnAfter.getCards().get(0), equalTo(secondCardToMove));
                            }
                            @Test
                            public void shouldPickMovedCardsFromTheStartOfTheFromColumn() {
                                Integer lastIndexWhereCardIsSet = toColumnAfter.getCards().size()-1;
                                assertThat(toColumnAfter.getCards().get(lastIndexWhereCardIsSet), equalTo(fromColumnFirstCardBefore));
                            }
                            @Test
                            public void shouldPlaceMovedCardsToEndOfTheToColumn() {
                                Integer lastIndexWhereCardIsSet = toColumnAfter.getCards().size()-1;
                                assertThat(toColumnAfter.getCards().get(lastIndexWhereCardIsSet), equalTo(firstCardToMove));
                            }
                        }
                    }

                    public class andPhaseHoldsFullAmountAllowedByWipLimit {
                        @Before
                        public void fillTheNewPhaseToWipLimitAndDoAction() {
                            initialGameContainer.fillDevelopmentInProgressToFullWip();
                            moveTestGameCards(fromColumnName, toColumnName);
                        }
                        @Test
                        public void shouldNotMoveAnyCardsToNewColumn() {
                            assertThat(toColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                        }
                        @Test
                        public void shouldRemainCardsInOldColumnIntact() {
                            assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                        }
                    }

                }

                public class andToColumnIsNotAdjacent {
                    @Before
                    public void setFromAndToColumnAndMoveCards() {
                        TestGameContainer.ColumnName fromColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                        TestGameContainer.ColumnName toColumnName = TestGameContainer.ColumnName.DEVELOPMENT_DONE;
                        moveTestGameCards(fromColumnName, toColumnName);
                    }
                    @Test
                    public void shouldNotMoveAnyCardsToNewColumn() {
                        assertThat(toColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                    }
                    @Test
                    public void shouldRemainCardsInOldColumnIntact() {
                        assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                    }
                }

            }

            public class whenPhaseIsNotAdjacentNextOne {
                @Before
                public void setFromAndToColumnAndMoveCards() {
                    TestGameContainer.ColumnName fromColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                    TestGameContainer.ColumnName toColumnName = TestGameContainer.ColumnName.TEST;
                    moveTestGameCards(fromColumnName, toColumnName);
                }

                @Test
                public void shouldNotMoveAnyCardsToNewColumn() {
                    assertThat(toColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                }
                @Test
                public void shouldRemainCardsInOldColumnIntact() {
                    assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                }
            }

        }

        public class withMoveCardsAction {
            public class withAllInvalidCardIds {

                @Before
                public void moveCards() {
                    TestGameContainer.ColumnName fromColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                    TestGameContainer.ColumnName toColumnName = TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS;

                    Column fromColumn = initialGameContainer.getColumn(fromColumnName);
                    Column toColumn = initialGameContainer.getColumn(toColumnName);

                    addMovingCardsIntoFromColumn(fromColumn);
                    List<MoveCardAction> moveCardActions = createMoveCardActions(fromColumn, toColumn);
                    moveCardActions.get(0).setCardId("wrongCardId1");
                    moveCardActions.get(1).setCardId("wrongCardId2");

                    performMoveCardsAndAssignResults(fromColumnName, toColumnName, moveCardActions);
                }

                @Test
                public void shouldNotMoveAnyCardsToNewColumn() {
                    assertThat(toColumnAfter.getCards(), not(hasItems(firstCardToMove, secondCardToMove)));
                }
                @Test
                public void shouldRemainCardsInOldColumnIntact() {
                    assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove, secondCardToMove));
                }
            }

            public class withSomeInvalidCardIds {

                @Before
                public void moveCards() {
                    TestGameContainer.ColumnName fromColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                    TestGameContainer.ColumnName toColumnName = TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS;

                    Column fromColumn = initialGameContainer.getColumn(fromColumnName);
                    Column toColumn = initialGameContainer.getColumn(toColumnName);

                    addMovingCardsIntoFromColumn(fromColumn);
                    List<MoveCardAction> moveCardActions = createMoveCardActions(fromColumn, toColumn);
                    moveCardActions.get(0).setCardId("wrongCardId");

                    performMoveCardsAndAssignResults(fromColumnName, toColumnName, moveCardActions);
                }

                @Test
                public void shouldLeaveTheFirstCardInOldColumn() {
                    assertThat(fromColumnAfter.getCards(), hasItems(firstCardToMove));
                    assertThat(fromColumnAfter.getCards(), not(hasItems(secondCardToMove)));
                }
                @Test
                public void shouldMoveTheOtherCardToNewColumn() {
                    assertThat(toColumnAfter.getCards(), not(hasItems(firstCardToMove)));
                    assertThat(toColumnAfter.getCards(), hasItems(secondCardToMove));
                }
            }

            public class withSomeInvalidToColumnIds {
                private TestGameContainer.ColumnName fromColumnName;
                private TestGameContainer.ColumnName toColumnName;
                private List<MoveCardAction> moveCardActions;

                @Before
                public void init() {
                    fromColumnName = TestGameContainer.ColumnName.ANALYSIS_DONE;
                    toColumnName = TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS;

                    Column fromColumn = initialGameContainer.getColumn(fromColumnName);
                    Column toColumn = initialGameContainer.getColumn(toColumnName);

                    addMovingCardsIntoFromColumn(fromColumn);
                    moveCardActions = createMoveCardActions(fromColumn, toColumn);
                    moveCardActions.get(0).setToColumnId("wrongColumnId");
                }

                @Rule
                public ExpectedException thrown = ExpectedException.none();

                @Test
                public void moveCards() {
                    thrown.expect(ColumnNotFoundException.class);
                    performMoveCardsAndAssignResults(fromColumnName, toColumnName, moveCardActions);
                }
            }
        }

        private void moveTestGameCards(TestGameContainer.ColumnName fromColumnName,
                                       TestGameContainer.ColumnName toColumnName) {
            Column fromColumn = initialGameContainer.getColumn(fromColumnName);
            Column toColumn = initialGameContainer.getColumn(toColumnName);

            addMovingCardsIntoFromColumn(fromColumn);
            List<MoveCardAction> moveCardActions = createMoveCardActions(fromColumn, toColumn);

            performMoveCardsAndAssignResults(fromColumnName, toColumnName, moveCardActions);
        }

        private void addMovingCardsIntoFromColumn(Column fromColumn) {
            Arrays.asList(firstCardToMove, secondCardToMove).forEach(cardToMove -> {
                if (!fromColumn.hasCard(cardToMove.getId())) {
                    fromColumn.getCards().add(cardToMove);
                }
            });
        }

        private List<MoveCardAction> createMoveCardActions(Column fromColumn, Column toColumn) {
            MoveCardAction firstMoveAction = MoveCardActionBuilder.aMoveCardAction()
                    .withFromColumnId(fromColumn.getId()).withToColumnId(toColumn.getId())
                    .withCardId(firstCardToMove.getId())
                    .build();
            MoveCardAction secondMoveAction = MoveCardActionBuilder.aMoveCardAction()
                    .withFromColumnId(fromColumn.getId()).withToColumnId(toColumn.getId())
                    .withCardId(secondCardToMove.getId())
                    .build();
            return Arrays.asList(firstMoveAction, secondMoveAction);
        }

        private void performMoveCardsAndAssignResults(TestGameContainer.ColumnName fromColumnName, TestGameContainer.ColumnName toColumnName, List<MoveCardAction> moveCardActions) {
            Game gameWithCardsMoved = actionExecutorService.moveCards(initialGameContainer.getGame(), moveCardActions);

            gameWithCardsMovedContainer = TestGameContainer.withGame(gameWithCardsMoved);
            fromColumnAfter = gameWithCardsMovedContainer.getColumn(fromColumnName);
            toColumnAfter = gameWithCardsMovedContainer.getColumn(toColumnName);
        }

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
                wipLimitAdjustedGame = actionExecutorService.adjustWipLimits(
                        initialGameContainer.getGame(), emptyWipLimitChangeAction);
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
                wipLimitAdjustedGame = actionExecutorService.adjustWipLimits(
                        initialGameContainer.getGame(), singlePhaseWipLimitChangeAction);
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
                wipLimitAdjustedGame = actionExecutorService.adjustWipLimits(
                        initialGame, singlePhaseWipLimitChangeAction);
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
            Game gameWithBacklogDrawn = actionExecutorService.drawFromBacklog(
                    initialGameContainer.getGame(), Arrays.asList(backlogAction));
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

    public class assignResources {
        private final Integer POINTS_BEFORE_ACTION = 0;

        private Phase phase;
        private Card firstCard;
        private Card firstCardAfter;

        @Before
        public void init() {
            phase = initialGameContainer.getDevelopmentPhase();
            initialGameContainer.addCardsWithMockPhasePointsToColumn(2,
                    TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS);
            firstCard = initialGameContainer.getColumn(TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS)
                    .getCards().get(0);
        }

        public class withCorrectPoints {
            private final Integer POINTS_TO_ADD = 2;

            @Before
            public void doAction() {
                assignResourcesAndFetchResults(POINTS_TO_ADD);
            }

            @Test
            public void shouldIncrementTotalPointsDone() {
                Integer pointsDone = firstCardAfter.getCardPhasePoints().stream()
                        .filter(cardPhasePoint -> cardPhasePoint.getPhaseId().equals(phase.getId()))
                        .findFirst().orElse(null).getPointsDone();
                assertThat(pointsDone, equalTo(POINTS_BEFORE_ACTION + POINTS_TO_ADD));
            }

            @Test
            public void shouldLeaveOtherPointsDoneIntact() {
                firstCardAfter.getCardPhasePoints().stream()
                        .filter(cardPhasePoint -> !cardPhasePoint.getPhaseId().equals(phase.getId()))
                        .peek(cardPhasePoint -> {
                            assertThat(cardPhasePoint.getPointsDone(), equalTo(POINTS_BEFORE_ACTION));
                        })
                        .toArray();
            }
        }

        private void assignResourcesAndFetchResults(Integer points) {
            List<AssignResourcesAction> actions = getActions(points);
            Game gameWithResourcesAssigned = actionExecutorService.assignResources(
                    initialGameContainer.getGame(), actions);
            TestGameContainer gameWithResourcesAssignedContainer
                    = TestGameContainer.withGame(gameWithResourcesAssigned);
            firstCardAfter = gameWithResourcesAssignedContainer
                    .getColumn(TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS).getCards().get(0);
        }

        private List<AssignResourcesAction> getActions(Integer points) {
            return Arrays.asList(AssignResourcesActionBuilder.anAssignResourcesAction()
                    .withCardId(firstCard.getId())
                    .withCardPhaseId(phase.getId())
                    .withPoints(points)
                    .build());
        }
    }

}

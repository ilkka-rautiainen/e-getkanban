package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.List;
import java.util.stream.Collectors;

import com.rits.cloning.Cloner;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.exceptions.PhaseNotFoundException;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.services.ai.movecards.MoveCardsAIService;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class MoveCardsAIServiceIntegrationTest {

    private static MoveCardsAIService moveCardsAIService;

    private TestGameContainer initialGameContainer;
    private TestGameContainer movedCardsGameContainer;

    @BeforeClass
    public static void setUpMoveCardsAIService() {
        moveCardsAIService = new MoveCardsAIService();
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
    }

    public class withLastWorkPhaseWithCardsInProgress {
        public class andLastWorkPhaseNotFull {
            @Before
            public void init() {
                initialGameContainer.fillLastWorkPhaseWithSomeCardsInProgress();
            }

            public class andFirstWorkPhasesWithCardsInProgress {
                private Game gameBefore;

                @Before
                public void initAndDoAction() throws PhaseNotFoundException {
                    initialGameContainer.fillFirstWorkPhasesWithSomeCardsInProgress();
                    gameBefore = new Cloner().deepClone(initialGameContainer.getGame());
                    performMoveCards();
                }

                @Test
                public void nothingShouldMoveEvenThoughTheLastWorkPhaseIsNotFull() {
                    assertThat(movedCardsGameContainer.getGame(), equalTo(gameBefore));
                }
            }
        }

        public class andLastWorkPhaseFull {
            @Before
            public void init() {
                initialGameContainer.fillLastWorkPhaseToFullWithCardsInProgress();
            }

            public class andFirstWorkPhasesReady {
                public class andFirstWorkPhasesFull {
                    private List<String> cardIdsInAnalysisBefore;
                    private List<String> cardIdsInDevelopmentBefore;
                    private List<String> cardIdsInTestBefore;

                    @Before
                    public void initAndDoAction() throws PhaseNotFoundException {
                        initialGameContainer.fillFirstWorkPhasesToFullWithReadyCards();
                        cardIdsInAnalysisBefore = getCardIds(initialGameContainer.getAnalysisPhase());
                        cardIdsInDevelopmentBefore = getCardIds(initialGameContainer.getDevelopmentPhase());
                        cardIdsInTestBefore = getCardIds(initialGameContainer.getTestPhase());
                        performMoveCards();
                    }

                    @Test
                    public void shouldRemainCardIdsInAnalysisIntact() {
                        List<String> cardIdsInAnalysisAfter = getCardIds(movedCardsGameContainer.getAnalysisPhase());
                        assertThat(cardIdsInAnalysisAfter, equalTo(cardIdsInAnalysisBefore));
                    }

                    @Test
                    public void shouldRemainCardIdsInDevelopmentIntact() {
                        List<String> cardIdsInDevelopmentAfter = getCardIds(movedCardsGameContainer.getDevelopmentPhase());
                        assertThat(cardIdsInDevelopmentAfter, equalTo(cardIdsInDevelopmentBefore));
                    }

                    @Test
                    public void shouldRemainCardIdsInTestIntact() {
                        List<String> cardIdsInTestAfter = getCardIds(movedCardsGameContainer.getTestPhase());
                        assertThat(cardIdsInTestAfter, equalTo(cardIdsInTestBefore));
                    }

                    @Test
                    public void shouldNotHaveExceededAnyWipLimit() {
                        assertWipLimitsNotExceeded();
                    }

                    private List<String> getCardIds(Phase phase) {
                        return phase.getColumns().stream()
                                .flatMap(column -> column.getCards().stream())
                                .map(Card::getId)
                                .sorted()
                                .collect(Collectors.toList());
                    }
                }

                public class andFirstWorkPhasesNotFull {
                    private Integer cardsInFirstWorkPhaseBefore;
                    private Integer cardsInSecondLastWorkPhaseBefore;
                    private Integer cardsInLastWorkPhaseBefore;

                    @Before
                    public void initAndDoAction() throws PhaseNotFoundException {
                        initialGameContainer.fillFirstWorkPhasesWithSomeReadyCards();
                        cardsInFirstWorkPhaseBefore = initialGameContainer.getAnalysisPhase().getTotalAmountOfCards();
                        cardsInSecondLastWorkPhaseBefore = initialGameContainer.getDevelopmentPhase()
                                .getTotalAmountOfCards();
                        cardsInLastWorkPhaseBefore = initialGameContainer.getTestPhase().getTotalAmountOfCards();
                        performMoveCards();
                    }

                    @Test
                    public void shouldMoveCardsFromFirstWorkPhase() {
                        Integer cardsInFirstWorkPhaseAfter = movedCardsGameContainer.getAnalysisPhase()
                                .getTotalAmountOfCards();
                        assertThat(cardsInFirstWorkPhaseAfter, lessThan(cardsInFirstWorkPhaseBefore));
                    }

                    @Test
                    public void shouldMoveCardsToSecondLastWorkPhase() {
                        Integer cardsInSecondLastWorkPhaseAfter = movedCardsGameContainer.getDevelopmentPhase()
                                .getTotalAmountOfCards();
                        assertThat(cardsInSecondLastWorkPhaseAfter, greaterThan(cardsInSecondLastWorkPhaseBefore));
                    }

                    @Test
                    public void shouldRemainCardsInLastWorkPhaseIntact() {
                        Integer cardsInLastWorkPhaseAfter = movedCardsGameContainer.getTestPhase()
                                .getTotalAmountOfCards();
                        assertThat(cardsInLastWorkPhaseAfter, equalTo(cardsInLastWorkPhaseBefore));
                    }

                    @Test
                    public void shouldNotHaveExceededAnyWipLimit() {
                        assertWipLimitsNotExceeded();
                    }
                }
            }
        }
    }

    public class withLastWorkPhaseReady {
        @Before
        public void init() {
            initialGameContainer.fillLastWorkPhaseWithSomeReadyCards();
        }

        public class andFirstWorkPhasesInProgress {
            private Integer cardsInFirstWorkPhaseBefore;
            private Integer cardsInSecondWorkPhaseBefore;

            @Before
            public void initAndDoAction() throws PhaseNotFoundException {
                initialGameContainer.fillFirstWorkPhasesWithSomeCardsInProgress();
                cardsInFirstWorkPhaseBefore = initialGameContainer.getAnalysisPhase().getTotalAmountOfCards();
                cardsInSecondWorkPhaseBefore = initialGameContainer.getDevelopmentPhase()
                        .getTotalAmountOfCards();
                performMoveCards();
            }

            @Test
            public void shouldRemainCardsInFirstWorkPhaseIntact() {
                Integer cardsInFirstWorkPhaseAfter = movedCardsGameContainer.getAnalysisPhase()
                        .getTotalAmountOfCards();
                assertThat(cardsInFirstWorkPhaseAfter, equalTo(cardsInFirstWorkPhaseBefore));
            }

            @Test
            public void shouldRemainCardsInSecondWorkPhaseIntact() {
                Integer cardsInSecondWorkPhaseAfter = movedCardsGameContainer.getDevelopmentPhase()
                        .getTotalAmountOfCards();
                assertThat(cardsInSecondWorkPhaseAfter, equalTo(cardsInSecondWorkPhaseBefore));
            }

            @Test
            public void shouldEmptyLastWorkPhase() {
                Integer cardsInLastWorkPhaseAfter = movedCardsGameContainer.getTestPhase().getTotalAmountOfCards();
                assertThat(cardsInLastWorkPhaseAfter, equalTo(0));
            }

            @Test
            public void shouldNotHaveExceededAnyWipLimit() {
                assertWipLimitsNotExceeded();
            }
        }

        public class andFirstWorkPhasesReady {
            private Integer cardsInSecondWorkPhaseBefore;
            private Integer cardsInLastWorkPhaseBefore;
            private Card cardInFirstWorkPhase;
            private Card cardInSecondWorkPhase;

            @Before
            public void initAndDoAction() throws PhaseNotFoundException {
                initialGameContainer.fillFirstWorkPhasesWithSomeReadyCards();
                cardsInSecondWorkPhaseBefore = initialGameContainer.getDevelopmentPhase().getTotalAmountOfCards();
                cardsInLastWorkPhaseBefore = initialGameContainer.getTestPhase().getTotalAmountOfCards();
                cardInFirstWorkPhase = initialGameContainer.getAnalysisPhase().getColumns().get(0).getCards().get(0);
                cardInSecondWorkPhase = initialGameContainer.getDevelopmentPhase()
                        .getColumns().get(0).getCards().get(0);
                performMoveCards();
            }

            @Test
            public void shouldEmptyFirstWorkPhase() {
                Integer cardsInFirstWorkPhaseAfter = movedCardsGameContainer.getAnalysisPhase()
                        .getTotalAmountOfCards();
                assertThat(cardsInFirstWorkPhaseAfter, equalTo(0));
            }

            @Test
            public void shouldSecondWorkPhaseLetOutAsManyCardsAsItReceives() {
                Integer cardsInSecondWorkPhaseAfter = movedCardsGameContainer.getDevelopmentPhase()
                        .getTotalAmountOfCards();
                assertThat(cardsInSecondWorkPhaseAfter, equalTo(cardsInSecondWorkPhaseBefore));
            }

            @Test
            public void shouldLastWorkPhaseLetOutAsManyCardsAsItReceives() {
                Integer cardsInLastWorkPhaseAfter = movedCardsGameContainer.getTestPhase().getTotalAmountOfCards();
                assertThat(cardsInLastWorkPhaseAfter, equalTo(cardsInLastWorkPhaseBefore));
            }

            @Test
            public void shouldMoveCardFromFirstWorkPhaseToSecond() {
                List<Card> cardsInSecondWorkPhaseFirstColumn = movedCardsGameContainer.getDevelopmentPhase()
                        .getColumns().get(0).getCards();
                assertThat(cardsInSecondWorkPhaseFirstColumn, contains(cardInFirstWorkPhase));
            }

            @Test
            public void shouldMoveCardFromSecondWorkPhaseToThird() {
                List<Card> cardsInThirdWorkPhaseFirstColumn = movedCardsGameContainer.getTestPhase()
                        .getColumns().get(0).getCards();
                assertThat(cardsInThirdWorkPhaseFirstColumn, contains(cardInSecondWorkPhase));
            }

            @Test
            public void shouldNotHaveExceededAnyWipLimit() {
                assertWipLimitsNotExceeded();
            }
        }
    }

    public class withLastWorkPhaseInProgressAndAlmostFull {
        @Before
        public void init() {
            initialGameContainer.fillLastWorkPhaseToAlmostFullWithCardsInProgress();
        }

        public class andFirstWorkPhasesReady {
            private Integer cardsInFirstWorkPhaseBefore;

            @Before
            public void initAndDoAction() throws PhaseNotFoundException {
                initialGameContainer.fillFirstWorkPhasesToFullWithReadyCards();
                cardsInFirstWorkPhaseBefore = initialGameContainer.getTestPhase().getTotalAmountOfCards();
                performMoveCards();
            }

            @Test
            public void shouldDecreaseOneCardFromFirstWorkPhase() {
                Integer cardsInFirstWorkPhaseAfter = movedCardsGameContainer.getAnalysisPhase().getTotalAmountOfCards();
                assertThat(cardsInFirstWorkPhaseAfter, equalTo(cardsInFirstWorkPhaseBefore - 1));
            }

            @Test
            public void shouldFillTheSecondLastWorkPhaseToFull() {
                Phase developmentPhase = movedCardsGameContainer.getDevelopmentPhase();
                Integer cardsInFirstWorkPhaseAfter = developmentPhase.getTotalAmountOfCards();
                assertThat(cardsInFirstWorkPhaseAfter, equalTo(developmentPhase.getWipLimit()));
            }

            @Test
            public void shouldFillTheLastWorkPhaseToFull() {
                Phase testPhase = movedCardsGameContainer.getTestPhase();
                Integer cardsInFirstWorkPhaseAfter = testPhase.getTotalAmountOfCards();
                assertThat(cardsInFirstWorkPhaseAfter, equalTo(testPhase.getWipLimit()));
            }

            @Test
            public void shouldNotHaveExceededAnyWipLimit() {
                assertWipLimitsNotExceeded();
            }
        }
    }

    private void performMoveCards() throws PhaseNotFoundException {
        List<MoveCardAction> actions = moveCardsAIService.getMoveCardsActions(initialGameContainer.getGame());
        Game gameWithCardsMoved = ActionExecutorService.moveCards(initialGameContainer.getGame(), actions);
        movedCardsGameContainer = TestGameContainer.withGame(gameWithCardsMoved);
    }

    private void assertWipLimitsNotExceeded() {
        assertThat(movedCardsGameContainer.getAnalysisPhase().getTotalAmountOfCards(),
                lessThanOrEqualTo(movedCardsGameContainer.getAnalysisPhase().getWipLimit()));
        assertThat(movedCardsGameContainer.getDevelopmentPhase().getTotalAmountOfCards(),
                lessThanOrEqualTo(movedCardsGameContainer.getDevelopmentPhase().getWipLimit()));
        assertThat(movedCardsGameContainer.getTestPhase().getTotalAmountOfCards(),
                lessThanOrEqualTo(movedCardsGameContainer.getTestPhase().getWipLimit()));
    }
}

package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static fi.aalto.ekanban.ApplicationConstants.*;

import java.util.List;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.builders.CFDBuilder;
import fi.aalto.ekanban.builders.CFDDailyValueBuilder;
import fi.aalto.ekanban.builders.CFDDailyValuesBuilder;
import fi.aalto.ekanban.models.*;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.utils.TestGameContainer;

@RunWith(HierarchicalContextRunner.class)
public class CFDCalculatorServiceIntegrationTest {

    private static CFDCalculatorService cfdCalculatorService;

    private TestGameContainer initialGameContainer;
    private Integer newLastDailyValueIdx;
    private CFDDailyValue newLastDailyValue;

    @BeforeClass
    public static void initService() {
        cfdCalculatorService = new CFDCalculatorService();
    }

    @Before
    public void init() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
    }

    public class withError {
        public class withInvalidAmountOfCFDDailyValues {
            @Before
            public void init() {
                initialGameContainer.getGame().getCFD().addCfdDailyValue(CFDDailyValueBuilder.aCFDDailyValue()
                        .withZeroValuesBasedOnPhases(initialGameContainer.getGame().getBoard().getPhases())
                        .build());
            }

            @Rule
            public ExpectedException thrown = ExpectedException.none();

            @Test
            public void shouldGiveAnErrorBecauseShouldntHaveTwoDailyValues() {
                thrown.expect(IllegalStateException.class);
                cfdCalculatorService.calculateCFDForCurrentDay(initialGameContainer.getGame());
            }
        }

        public class withInvalidPhasesInCFDDailyValues {
            @Before
            public void init() {
                initialGameContainer.getGame().setCurrentDay(1);
                initialGameContainer.getGame().setCFD(getCFDWithInvalidPhasesInDailyValues());
            }

            private CFD getCFDWithInvalidPhasesInDailyValues() {
                CFD cfd = CFDBuilder.aCFD().build();
                List<CFDDailyValue> dailyValuesWithInvalidPhases = CFDDailyValuesBuilder.aSetOfCfdDailyValues()
                        .withOneDailyValueWithZerosBasedOnPhases(initialGameContainer.getGame().getBoard().getPhases())
                        .build();
                dailyValuesWithInvalidPhases.get(0).getPhaseValues().remove(ANALYSIS_PHASE_ID);
                cfd.setCfdDailyValues(dailyValuesWithInvalidPhases);
                return cfd;
            }

            @Rule
            public ExpectedException thrown = ExpectedException.none();

            @Test
            public void shouldGiveAnErrorBecauseAnalysisPhaseIdIsMissing() {
                thrown.expect(IllegalStateException.class);
                cfdCalculatorService.calculateCFDForCurrentDay(initialGameContainer.getGame());
            }
        }
    }

    public class withCardsOnBoard {
        public class withCardsInDeployedAndDevelopmentFirstColumn {
            private final Integer CURRENT_DAY = 5;
            private final Integer DEPLOYED_CARDS = 10;
            private final Integer DEV_IN_PROGRESS_CARDS = 2;
            @Before
            public void initAndDoAction() {
                initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
                initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(DEPLOYED_CARDS,
                        TestGameContainer.ColumnName.DEPLOYED);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(DEV_IN_PROGRESS_CARDS,
                        TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS);
                calculateCFDAndSaveResult();
            }

            @Test
            public void shouldAddCFDForTheRightDay() {
                assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
            }

            @Test
            public void shouldHaveDeployedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(DEPLOYED_CARDS));
            }

            @Test
            public void shouldHaveDevelopedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(DEPLOYED_CARDS));
            }

            @Test
            public void shouldHaveAnalysedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(DEV_IN_PROGRESS_CARDS
                        + DEPLOYED_CARDS));
            }

            @Test
            public void shouldHaveEnteredBoardRightAmount() {
                assertThat(newLastDailyValue.getEnteredBoard(), equalTo(DEV_IN_PROGRESS_CARDS
                        + DEPLOYED_CARDS));
            }
        }

        public class withCardsInDeployedAndDevelopmentSecondColumn {
            private final Integer CURRENT_DAY = 6;
            private final Integer DEPLOYED_CARDS = 8;
            private final Integer DEV_DONE_CARDS = 1;
            @Before
            public void initAndDoAction() {
                initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
                initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(DEPLOYED_CARDS,
                        TestGameContainer.ColumnName.DEPLOYED);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(DEV_DONE_CARDS,
                        TestGameContainer.ColumnName.DEVELOPMENT_DONE);
                calculateCFDAndSaveResult();
            }

            @Test
            public void shouldAddCFDForTheRightDay() {
                assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
            }

            @Test
            public void shouldHaveDeployedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(DEPLOYED_CARDS));
            }

            @Test
            public void shouldHaveDevelopedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(DEV_DONE_CARDS
                        + DEPLOYED_CARDS));
            }

            @Test
            public void shouldHaveAnalysedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(DEV_DONE_CARDS
                        + DEPLOYED_CARDS));
            }

            @Test
            public void shouldHaveEnteredBoardRightAmount() {
                assertThat(newLastDailyValue.getEnteredBoard(), equalTo(DEV_DONE_CARDS
                        + DEPLOYED_CARDS));
            }
        }

        public class withCardsInTestAndDevelopmentSecondColumn {
            private final Integer CURRENT_DAY = 2;
            private final Integer TEST_CARDS = 7;
            private final Integer DEV_DONE_CARDS = 2;

            @Before
            public void initAndDoAction() {
                initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
                initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(TEST_CARDS,
                        TestGameContainer.ColumnName.TEST);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(DEV_DONE_CARDS,
                        TestGameContainer.ColumnName.DEVELOPMENT_DONE);
                calculateCFDAndSaveResult();
            }

            @Test
            public void shouldAddCFDForTheRightDay() {
                assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
            }

            @Test
            public void shouldHaveDeployedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(0));
            }

            @Test
            public void shouldHaveDevelopedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(DEV_DONE_CARDS
                        + TEST_CARDS));
            }

            @Test
            public void shouldHaveAnalysedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(DEV_DONE_CARDS
                        + TEST_CARDS));
            }

            @Test
            public void shouldHaveEnteredBoardRightAmount() {
                assertThat(newLastDailyValue.getEnteredBoard(), equalTo(DEV_DONE_CARDS
                        + TEST_CARDS));
            }
        }

        public class withCardsInAnalysisSecondAndDevelopmentFirstColumn {
            private final Integer CURRENT_DAY = 20;
            private final Integer ANALYSIS_DONE_CARDS = 1;
            private final Integer DEV_IN_PROGRESS_CARDS = 2;

            @Before
            public void initAndDoAction() {
                initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
                initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(ANALYSIS_DONE_CARDS,
                        TestGameContainer.ColumnName.ANALYSIS_DONE);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(DEV_IN_PROGRESS_CARDS,
                        TestGameContainer.ColumnName.DEVELOPMENT_IN_PROGRESS);
                calculateCFDAndSaveResult();
            }

            @Test
            public void shouldAddCFDForTheRightDay() {
                assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
            }

            @Test
            public void shouldHaveDeployedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(0));
            }

            @Test
            public void shouldHaveDevelopedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(0));
            }

            @Test
            public void shouldHaveAnalysedRightAmount() {
                assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(ANALYSIS_DONE_CARDS
                        + DEV_IN_PROGRESS_CARDS));
            }

            @Test
            public void shouldHaveEnteredBoardRightAmount() {
                assertThat(newLastDailyValue.getEnteredBoard(), equalTo(ANALYSIS_DONE_CARDS
                        + DEV_IN_PROGRESS_CARDS));
            }
        }

        public class withCardsInAnalysisFirstAndAnalysisSecondColumn {
            private final Integer ANALYSIS_IN_PROGRESS_CARDS = 1;
            private final Integer ANALYSIS_DONE_CARDS = 1;

            @Before
            public void initAndDoAction() {
                initialGameContainer.addCardsWithMockPhasePointsToColumn(ANALYSIS_IN_PROGRESS_CARDS,
                        TestGameContainer.ColumnName.ANALYSIS_IN_PROGRESS);
                initialGameContainer.addCardsWithMockPhasePointsToColumn(ANALYSIS_DONE_CARDS,
                        TestGameContainer.ColumnName.ANALYSIS_DONE);
            }

            public class withCardsInDeployed {
                private final Integer CURRENT_DAY = 18;
                private final Integer DEPLOYED_CARDS = 3;

                @Before
                public void initAndDoAction() {
                    initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
                    initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
                    initialGameContainer.addCardsWithMockPhasePointsToColumn(DEPLOYED_CARDS,
                            TestGameContainer.ColumnName.DEPLOYED);
                    calculateCFDAndSaveResult();
                }

                @Test
                public void shouldAddCFDForTheRightDay() {
                    assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
                }

                @Test
                public void shouldHaveDeployedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(DEPLOYED_CARDS));
                }

                @Test
                public void shouldHaveDevelopedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(DEPLOYED_CARDS));
                }

                @Test
                public void shouldHaveAnalysedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(ANALYSIS_DONE_CARDS
                            + DEPLOYED_CARDS));
                }

                @Test
                public void shouldHaveEnteredBoardRightAmount() {
                    assertThat(newLastDailyValue.getEnteredBoard(), equalTo(ANALYSIS_IN_PROGRESS_CARDS
                            + ANALYSIS_DONE_CARDS + DEPLOYED_CARDS));
                }
            }

            public class withoutCardsInDeployed {
                private final Integer CURRENT_DAY = 18;

                @Before
                public void initAndDoAction() {
                    initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
                    initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
                    calculateCFDAndSaveResult();
                }

                @Test
                public void shouldAddCFDForTheRightDay() {
                    assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
                }

                @Test
                public void shouldHaveDeployedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(0));
                }

                @Test
                public void shouldHaveDevelopedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(0));
                }

                @Test
                public void shouldHaveAnalysedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(ANALYSIS_DONE_CARDS));
                }

                @Test
                public void shouldHaveEnteredBoardRightAmount() {
                    assertThat(newLastDailyValue.getEnteredBoard(), equalTo(ANALYSIS_IN_PROGRESS_CARDS
                            + ANALYSIS_DONE_CARDS));
                }
            }

            public class withCardsInTestAndDeployed {
                private final Integer CURRENT_DAY = 18;
                private final Integer TEST_CARDS = 2;
                private final Integer DEPLOYED_CARDS = 3;

                @Before
                public void initAndDoAction() {
                    initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
                    initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
                    initialGameContainer.addCardsWithMockPhasePointsToColumn(TEST_CARDS,
                            TestGameContainer.ColumnName.TEST);
                    initialGameContainer.addCardsWithMockPhasePointsToColumn(DEPLOYED_CARDS,
                            TestGameContainer.ColumnName.DEPLOYED);
                    calculateCFDAndSaveResult();
                }

                @Test
                public void shouldAddCFDForTheRightDay() {
                    assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
                }

                @Test
                public void shouldHaveDeployedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(DEPLOYED_CARDS));
                }

                @Test
                public void shouldHaveDevelopedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(DEPLOYED_CARDS
                            + TEST_CARDS));
                }

                @Test
                public void shouldHaveAnalysedRightAmount() {
                    assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(ANALYSIS_DONE_CARDS
                            + TEST_CARDS + DEPLOYED_CARDS));
                }

                @Test
                public void shouldHaveEnteredBoardRightAmount() {
                    assertThat(newLastDailyValue.getEnteredBoard(), equalTo(ANALYSIS_IN_PROGRESS_CARDS
                            + ANALYSIS_DONE_CARDS + TEST_CARDS + DEPLOYED_CARDS));
                }
            }
        }
    }

    public class withNoCardsOnBoard {
        private final Integer CURRENT_DAY = 5;

        @Before
        public void initAndDoAction() {
            initialGameContainer.getGame().setCurrentDay(CURRENT_DAY);
            initialGameContainer.fillCFDWithZeroValuesUntilDay(CURRENT_DAY - 1);
            calculateCFDAndSaveResult();
        }

        @Test
        public void shouldAddCFDForTheRightDay() {
            assertThat(newLastDailyValueIdx, equalTo(CURRENT_DAY));
        }

        @Test
        public void shouldBeZeroEnteredBoard() {
            assertThat(newLastDailyValue.getEnteredBoard(), equalTo(0));
        }

        @Test
        public void shouldBeZeroAnalysisReady() {
            assertThat(newLastDailyValue.getPhaseValues().get(ANALYSIS_PHASE_ID), equalTo(0));
        }

        @Test
        public void shouldBeZeroDevelopmentReady() {
            assertThat(newLastDailyValue.getPhaseValues().get(DEVELOPMENT_PHASE_ID), equalTo(0));
        }

        @Test
        public void shouldBeZeroTestReady() {
            assertThat(newLastDailyValue.getPhaseValues().get(DEPLOYED_PHASE_ID), equalTo(0));
        }
    }

    private void calculateCFDAndSaveResult() {
        Game newGame = cfdCalculatorService.calculateCFDForCurrentDay(initialGameContainer.getGame());
        newLastDailyValueIdx = newGame.getCFD().getCfdDailyValues().size() - 1;
        newLastDailyValue = newGame.getCFD().getCfdDailyValues().get(newLastDailyValueIdx);
    }
}

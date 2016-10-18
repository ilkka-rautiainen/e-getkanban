package fi.aalto.ekanban.services;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static fi.aalto.ekanban.services.TestDataInitializer.initNormalDifficultyGame;
import static fi.aalto.ekanban.services.PlayerService.adjustWipLimits;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
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

    @BeforeClass
    public static void setUpPlayerService() {
        playerService = new PlayerService();
    }

    public class adjustWipLimits {

        private Game initializedGame;
        private Game wipLimitAdjustedGame;
        private Map<String, Integer> phaseWipLimits;
        private Random random;

        @Before
        public void initGame() {
            initializedGame = initNormalDifficultyGame();
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
                originalWipLimits = initializedGame.getBoard().getPhases().stream()
                    .filter(phase -> phase.getWipLimit() != null)
                    .mapToInt(phase -> phase.getWipLimit()).toArray();

                AdjustWipLimitsAction emptyWipLimitChangeAction = null;
                wipLimitAdjustedGame = adjustWipLimits(initializedGame, emptyWipLimitChangeAction);
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
                phaseId = initializedGame.getBoard().getPhases().get(randomlySelectedPhase).getId();
                newWipLimit = 5;
                phaseWipLimits.put(phaseId, newWipLimit);
                AdjustWipLimitsAction singlePhaseWipLimitChangeAction =
                        AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction()
                                .withPhaseWipLimits(phaseWipLimits)
                                .build();
                wipLimitAdjustedGame = adjustWipLimits(initializedGame, singlePhaseWipLimitChangeAction);
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
                String analysisPhaseId = initializedGame.getBoard().getPhases().get(0).getId();
                String developmentPhaseId = initializedGame.getBoard().getPhases().get(1).getId();
                String testPhaseId = initializedGame.getBoard().getPhases().get(2).getId();
                phaseWipLimits.put(analysisPhaseId, newWipLimitOfAnalysisPhase);
                phaseWipLimits.put(developmentPhaseId, newWipLimitOfDevelopmentPhase);
                phaseWipLimits.put(testPhaseId, newWipLimitOfTestPhase);

                AdjustWipLimitsAction singlePhaseWipLimitChangeAction =
                        AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction()
                                .withPhaseWipLimits(phaseWipLimits)
                                .build();
                wipLimitAdjustedGame = adjustWipLimits(initializedGame, singlePhaseWipLimitChangeAction);
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

}

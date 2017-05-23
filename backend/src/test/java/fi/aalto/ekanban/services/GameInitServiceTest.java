package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE_ID;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;


@RunWith(HierarchicalContextRunner.class)
public class GameInitServiceTest {

    public class enableCardThreeDayMoveCycle {

        private Game gameWithMoveCycleEnabled;
        private Integer defaultDayMultiplierToEnter = 1;
        private Column inProgressColumnOfAnalysis;
        private Column firstColumnOfDeployed;

        @Before
        public void initContextAndDoAction() {
            GameInitService gameInitService = new GameInitService();
            Game defaultGame = GameBuilder.aGame().withNormalDifficultyMockDefaults("Player").build();
            gameWithMoveCycleEnabled = gameInitService.enableCardThreeDayMoveCycle(defaultGame);
            inProgressColumnOfAnalysis = gameWithMoveCycleEnabled.getBoard().getPhaseWithId(ANALYSIS_PHASE_ID).getFirstColumn();
            firstColumnOfDeployed = gameWithMoveCycleEnabled.getBoard().getPhaseWithId(DEPLOYED_PHASE_ID).getFirstColumn();
        }

        @Test
        public void shouldSetDayMultiplierToEnterOfInProgressColumnOfAnalysisToThree() {
            assertThat(inProgressColumnOfAnalysis.getDayMultiplierToEnter(), equalTo(3));
        }

        @Test
        public void shouldSetDayMultiplierToEnterOfFirstColumnOfDeployedToThree() {
            assertThat(firstColumnOfDeployed.getDayMultiplierToEnter(), equalTo(3));
        }

        @Test
        public void shouldNotChangeDayMultiplierToEnterOfOtherColumns() {
            gameWithMoveCycleEnabled.getBoard().getPhases().forEach(
                phase -> {
                    phase.getColumns().forEach(column -> {
                      if (!column.equals(inProgressColumnOfAnalysis) && ! column.equals(firstColumnOfDeployed)) {
                          assertThat(column.getDayMultiplierToEnter(), equalTo(defaultDayMultiplierToEnter));
                      }
                    });
                }
            );
        }

    }

}

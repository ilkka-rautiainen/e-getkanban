package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.TEST_PHASE_ID;

import java.util.List;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import fi.aalto.ekanban.models.DiceCastAction;
import fi.aalto.ekanban.services.ai.assignresources.DiceCastService;
import fi.aalto.ekanban.services.dice.DiceService;
import fi.aalto.ekanban.services.dice.DiceServiceMock;
import fi.aalto.ekanban.utils.TestGameContainer;


@RunWith(HierarchicalContextRunner.class)
public class DiceCastServiceTest {
    private static TestGameContainer testGameContainer;
    private static DiceCastService diceCastService;
    private List<DiceCastAction> diceCastActions;

    @BeforeClass
    public static void init() {
        testGameContainer = TestGameContainer.withNormalDifficultyMockGame();
        DiceService diceServiceMock = new DiceServiceMock();
        diceCastService = new DiceCastService(diceServiceMock);
    }

    @Before
    public void doAction() {
        diceCastActions = diceCastService.getDiceCastActions(testGameContainer.getGame());
    }

    @Test
    public void shouldCreateThreeActions() {
        assertThat(diceCastActions.size(), equalTo(3));
    }

    @Test
    public void shouldCreateOneActionsForAnalysis() {
        Integer analysisActions = (int)diceCastActions.stream()
                .filter(action -> action.getPhaseId().equals(ANALYSIS_PHASE_ID))
                .count();
        assertThat(analysisActions, equalTo(1));
    }

    @Test
    public void shouldCastTwoDiceForAnalysis() {
        DiceCastAction analysisAction = diceCastActions.stream()
                .filter(action -> action.getPhaseId().equals(ANALYSIS_PHASE_ID))
                .findFirst().orElse(null);
        assertThat(analysisAction.getDiceValues().size(), equalTo(2));
    }

    @Test
    public void shouldCreateOneActionsForDevelopment() {
        Integer developmentActions = (int)diceCastActions.stream()
                .filter(action -> action.getPhaseId().equals(DEVELOPMENT_PHASE_ID))
                .count();
        assertThat(developmentActions, equalTo(1));
    }

    @Test
    public void shouldCastThreeDiceForDevelopment() {
        DiceCastAction developmentAction = diceCastActions.stream()
                .filter(action -> action.getPhaseId().equals(DEVELOPMENT_PHASE_ID))
                .findFirst().orElse(null);
        assertThat(developmentAction.getDiceValues().size(), equalTo(3));
    }

    @Test
    public void shouldCreateOneActionsForTest() {
        Integer testActions = (int)diceCastActions.stream()
                .filter(action -> action.getPhaseId().equals(TEST_PHASE_ID))
                .count();
        assertThat(testActions, equalTo(1));
    }

    @Test
    public void shouldCastTwoDiceForTest() {
        DiceCastAction testAction = diceCastActions.stream()
                .filter(action -> action.getPhaseId().equals(TEST_PHASE_ID))
                .findFirst().orElse(null);
        assertThat(testAction.getDiceValues().size(), equalTo(2));
    }
}

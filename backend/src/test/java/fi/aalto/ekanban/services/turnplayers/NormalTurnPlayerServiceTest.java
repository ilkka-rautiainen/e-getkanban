package fi.aalto.ekanban.services.turnplayers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

import java.util.Arrays;
import java.util.List;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import fi.aalto.ekanban.builders.AdjustWipLimitsActionBuilder;
import fi.aalto.ekanban.builders.AssignResourcesActionBuilder;
import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.services.ActionExecutorService;
import fi.aalto.ekanban.services.ai.assignresources.AssignResourcesAIService;
import fi.aalto.ekanban.services.ai.drawfrombacklog.DrawFromBacklogAIService;
import fi.aalto.ekanban.services.ai.movecards.MoveCardsAIService;


@RunWith(HierarchicalContextRunner.class)
public class NormalTurnPlayerServiceTest {

    @InjectMocks
    private static NormalTurnPlayerService normalTurnPlayerService;
    @Mock
    private static ActionExecutorService actionExecutorService;
    @Mock
    private static AssignResourcesAIService assignResourcesAIService;
    @Mock
    private static MoveCardsAIService moveCardsAIService;
    @Mock
    private static DrawFromBacklogAIService drawFromBacklogAIService;

    @BeforeClass
    public static void initService() { normalTurnPlayerService = new NormalTurnPlayerService();}

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    public class playTurn {

        private Game gameWithTurnPlayed;
        private List<AssignResourcesAction> assignResourcesActions;

        @Before
        public void setupContextAndCallMethod() {
            Game gameBeforePlayingTurn = GameBuilder.aGame()
                    .withNormalDifficultyMockDefaults("Player")
                    .build();

            Turn turnWithAdjustWipLimits = TurnBuilder.aTurn()
                    .withAdjustWipLimitsAction(AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction().build())
                    .build();
            assignResourcesActions = Arrays.asList(AssignResourcesActionBuilder.anAssignResourcesAction().build(),
                                                   AssignResourcesActionBuilder.anAssignResourcesAction().build());

            Mockito.when(actionExecutorService
                    .adjustWipLimits(gameBeforePlayingTurn, turnWithAdjustWipLimits.getAdjustWipLimitsAction()))
                    .thenReturn(gameBeforePlayingTurn);
            Mockito.when(assignResourcesAIService.getAssignResourcesActions(gameBeforePlayingTurn))
                    .thenReturn(assignResourcesActions);
            Mockito.when(actionExecutorService.assignResources(gameBeforePlayingTurn, assignResourcesActions))
                    .thenReturn(gameBeforePlayingTurn);
            Mockito.when(moveCardsAIService.getMoveCardsActions(gameBeforePlayingTurn)).thenReturn(null);
            Mockito.when(actionExecutorService.moveCards(Mockito.any(Game.class), Mockito.anyList()))
                    .thenReturn(gameBeforePlayingTurn);
            Mockito.when(drawFromBacklogAIService.getDrawFromBacklogActions(gameBeforePlayingTurn)).thenReturn(null);
            Mockito.when(actionExecutorService.drawFromBacklog(Mockito.any(Game.class), Mockito.anyList()))
                    .thenReturn(gameBeforePlayingTurn);

            gameWithTurnPlayed = normalTurnPlayerService.playTurn(gameBeforePlayingTurn, turnWithAdjustWipLimits);
        }

        @Test
        public void shouldCallAssignResourcesAIService() {
            Mockito.verify(assignResourcesAIService, Mockito.times(1)).getAssignResourcesActions(Mockito.any(Game.class));
        }

        @Test
        public void shouldCallMoveCardsAIService() {
            Mockito.verify(moveCardsAIService, Mockito.times(1)).getMoveCardsActions(Mockito.any(Game.class));
        }

        @Test
        public void shouldCallDrawFromBacklogAIService() {
            Mockito.verify(drawFromBacklogAIService, Mockito.times(1)).getDrawFromBacklogActions(Mockito.any(Game.class));
        }

        @Test
        public void shouldCallActionExecutorServiceMethods() {
            Mockito.verify(actionExecutorService, Mockito.times(1))
                    .adjustWipLimits(Mockito.any(Game.class), Mockito.any(AdjustWipLimitsAction.class));
            Mockito.verify(actionExecutorService, Mockito.times(1))
                    .assignResources(Mockito.any(Game.class), Mockito.anyList());
            Mockito.verify(actionExecutorService, Mockito.times(1))
                    .moveCards(Mockito.any(Game.class), Mockito.anyList());
            Mockito.verify(actionExecutorService, Mockito.times(1))
                    .drawFromBacklog(Mockito.any(Game.class), Mockito.anyList());
        }

        @Test
        public void shouldSetLatestAssignResourcesActionsForGame() {
            assertThat(gameWithTurnPlayed.getLatestAssignResourcesActions(), not(equalTo(null)));
            assertThat(gameWithTurnPlayed.getLatestAssignResourcesActions(), equalTo(assignResourcesActions));
        }

    }

}

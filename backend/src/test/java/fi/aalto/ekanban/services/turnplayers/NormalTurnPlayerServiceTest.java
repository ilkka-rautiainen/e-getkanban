package fi.aalto.ekanban.services.turnplayers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

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

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.models.*;
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

        private Game game;
        private List<AssignResourcesAction> assignResourcesActions;
        private List<MoveCardAction> moveCardsActions;
        private List<DrawFromBacklogAction> drawFromBacklogActions;

        @Before
        public void setupContextAndCallMethod() {
            game = GameBuilder.aGame()
                    .withNormalDifficultyMockDefaults("Player")
                    .build();

            Turn turnWithAdjustWipLimits = TurnBuilder.aTurn()
                    .withAdjustWipLimitsAction(AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction().build())
                    .build();

            assignResourcesActions = Arrays.asList(AssignResourcesActionBuilder.anAssignResourcesAction().build(),
                    AssignResourcesActionBuilder.anAssignResourcesAction().build());
            moveCardsActions = Arrays.asList(MoveCardActionBuilder.aMoveCardAction().build(),
                    MoveCardActionBuilder.aMoveCardAction().build());
            drawFromBacklogActions = Arrays.asList(DrawFromBacklogActionBuilder.aDrawFromBacklogAction().build(),
                    DrawFromBacklogActionBuilder.aDrawFromBacklogAction().build());

            Mockito.when(assignResourcesAIService.getAssignResourcesActions(game)).thenReturn(assignResourcesActions);
            Mockito.when(moveCardsAIService.getMoveCardsActions(game)).thenReturn(moveCardsActions);
            Mockito.when(drawFromBacklogAIService.getDrawFromBacklogActions(game)).thenReturn(drawFromBacklogActions);

            normalTurnPlayerService.playTurn(game, turnWithAdjustWipLimits);
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
        public void shouldSetLastTurnForGame() {
            assertThat(game.getLastTurn(), is(notNullValue()));
        }

        @Test
        public void shouldSetAssignResourcesActions() {
            assertThat(game.getLastTurn().getAssignResourcesActions(), equalTo(assignResourcesActions));
        }

        @Test
        public void shouldSetMoveCardsActions() {
            assertThat(game.getLastTurn().getMoveCardActions(), equalTo(moveCardsActions));
        }

        @Test
        public void shouldSetDrawFromBacklogActions() {
            assertThat(game.getLastTurn().getDrawFromBacklogActions(), equalTo(drawFromBacklogActions));
        }

    }

}

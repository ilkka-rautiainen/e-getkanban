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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.models.*;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.services.ActionExecutorService;
import fi.aalto.ekanban.services.ai.assignresources.AssignResourcesAIService;
import fi.aalto.ekanban.services.ai.assignresources.DiceCastService;
import fi.aalto.ekanban.services.ai.drawfrombacklog.DrawFromBacklogAIService;
import fi.aalto.ekanban.services.ai.movecards.MoveCardsAIService;


@RunWith(HierarchicalContextRunner.class)
public class MediumTurnPlayerServiceTest {

    @InjectMocks
    private static MediumTurnPlayerService mediumTurnPlayerService;
    @Mock
    private static ActionExecutorService actionExecutorService;
    @Mock
    private static AssignResourcesAIService assignResourcesAIService;
    @Mock
    private static MoveCardsAIService moveCardsAIService;
    @Mock
    private static DrawFromBacklogAIService drawFromBacklogAIService;
    @Mock
    private static DiceCastService diceCastService;

    @BeforeClass
    public static void initService() { mediumTurnPlayerService = new MediumTurnPlayerService();}

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    public class playTurn {

        private Game game;
        private List<AssignResourcesAction> assignResourcesActions;
        private List<MoveCardAction> moveCardsActions;
        private List<DrawFromBacklogAction> drawFromBacklogActions;
        private List<DiceCastAction> diceCastActions;

        private Card cardInAnalysis;
        private Card secondCardInDevelopment;

        @Before
        public void setupContextAndCallMethod() {

            Integer dayWhenAllActionsShouldHappen = 2;
            game = GameBuilder.aGame()
                    .withNormalDifficultyMockDefaults("Player")
                    .withCurrentDay(dayWhenAllActionsShouldHappen)
                    .build();

            Phase analysisPhase = game.getBoard().getWorkPhases().get(0);
            Phase developmentPhase = game.getBoard().getWorkPhases().get(1);

            Column analysisInProgressColumn = analysisPhase.getFirstColumn();
            Column developmentInProgressColumn = developmentPhase.getFirstColumn();

            cardInAnalysis = CardBuilder.aCard().withMockPhasePoints().build();
            Card cardInDevelopment = CardBuilder.aCard().withMockPhasePoints().build();
            secondCardInDevelopment = CardBuilder.aCard().withMockPhasePoints().build();

            analysisInProgressColumn.setCards(Arrays.asList(cardInAnalysis));
            developmentInProgressColumn.setCards(Arrays.asList(cardInDevelopment, secondCardInDevelopment));

            AssignResourcesAction assignResourcesActionWithDevelopmentDieToAnalysisCard =
                    AssignResourcesActionBuilder.anAssignResourcesAction()
                        .withCardId(cardInAnalysis.getId())
                        .withCardPhaseId(game.getBoard().getWorkPhases().get(0).getId())
                        .withDieIndex(1)
                        .withDiePhaseId(developmentPhase.getId())
                        .build();

            AssignResourcesAction assignResourcesActionWithAnalysisDieToDevelopmentCard =
                    AssignResourcesActionBuilder.anAssignResourcesAction()
                        .withCardId(secondCardInDevelopment.getId())
                        .withCardPhaseId(game.getBoard().getWorkPhases().get(1).getId())
                        .withDieIndex(0)
                        .withDiePhaseId(analysisPhase.getId())
                        .build();

            assignResourcesActions = Arrays.asList(
                    assignResourcesActionWithAnalysisDieToDevelopmentCard,
                    assignResourcesActionWithDevelopmentDieToAnalysisCard
            );

            Turn turnWithAdjustWipLimits = TurnBuilder.aTurn()
                    .withAdjustWipLimitsAction(AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction().build())
                    .withAssignResourcesActions(assignResourcesActions)
                    .build();

            moveCardsActions = Arrays.asList(MoveCardActionBuilder.aMoveCardAction().build(),
                    MoveCardActionBuilder.aMoveCardAction().build());
            drawFromBacklogActions = Arrays.asList(DrawFromBacklogActionBuilder.aDrawFromBacklogAction().build(),
                    DrawFromBacklogActionBuilder.aDrawFromBacklogAction().build());
            diceCastActions = Arrays.asList(
                    DiceCastActionBuilder.aDiceCastAction()
                            .withPhaseId(analysisPhase.getId()).withDice(Arrays.asList(
                                DieBuilder.aDie().withPrimaryValue(5).withFirstSecondaryValue(2).withSecondSecondaryValue(2).build()
                    )).build(),
                    DiceCastActionBuilder.aDiceCastAction()
                            .withPhaseId(developmentPhase.getId()).withDice(Arrays.asList(
                                DieBuilder.aDie().withPrimaryValue(3).withFirstSecondaryValue(2).withSecondSecondaryValue(2).build(),
                                DieBuilder.aDie().withPrimaryValue(5).withFirstSecondaryValue(1).withSecondSecondaryValue(2).build()
                    )).build()
            );

            Mockito.when(actionExecutorService.adjustWipLimits(
                    Mockito.any(Game.class), Mockito.any(AdjustWipLimitsAction.class))).thenReturn(game);
            Mockito.when(actionExecutorService.assignResources(
                    Mockito.any(Game.class), Mockito.anyList())).thenReturn(game);
            Mockito.when(actionExecutorService.moveCards(
                    Mockito.any(Game.class), Mockito.anyList())).thenReturn(game);
            Mockito.when(actionExecutorService.drawFromBacklog(
                    Mockito.any(Game.class), Mockito.anyList())).thenReturn(game);
            Mockito.when(moveCardsAIService.getMoveCardsActions(game)).thenReturn(moveCardsActions);
            Mockito.when(drawFromBacklogAIService.getDrawFromBacklogActions(game)).thenReturn(drawFromBacklogActions);
            Mockito.when(diceCastService.getDiceCastActions(game)).thenReturn(diceCastActions);

            mediumTurnPlayerService.playTurn(game, turnWithAdjustWipLimits);
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
        public void shouldSetDiceCastActions() {
            assertThat(game.getLastTurn().getDiceCastActions(), equalTo(diceCastActions));
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

package fi.aalto.ekanban.services.turnplayers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.services.ai.assignresources.AssignResourcesAIService;
import fi.aalto.ekanban.services.ai.assignresources.DiceCastService;
import fi.aalto.ekanban.services.ai.drawfrombacklog.DrawFromBacklogAIService;
import fi.aalto.ekanban.services.ai.movecards.MoveCardsAIService;
import fi.aalto.ekanban.services.ActionExecutorService;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.*;

@Service
public class NormalTurnPlayerService implements TurnPlayer {

    @Autowired
    private ActionExecutorService actionExecutorService;

    @Autowired
    private AssignResourcesAIService assignResourcesAIService;

    @Autowired
    private MoveCardsAIService moveCardsAIService;

    @Autowired
    private DrawFromBacklogAIService drawFromBacklogAIService;

    @Autowired
    private DiceCastService diceCastService;

    public Game playTurn(Game game, Turn turn) {
        game.setLastTurn(turn);
        game = actionExecutorService.adjustWipLimits(game, turn.getAdjustWipLimitsAction());
        game = assignResourcesWithAI(game);
        game = moveCardsWithAI(game);
        game = drawFromBacklogWithAI(game);
        return game;
    }

    private Game assignResourcesWithAI(Game game) {
        List<DiceCastAction> diceCastActions = diceCastService.getDiceCastActions(game);
        List<AssignResourcesAction> assignResourcesActions = assignResourcesAIService.getAssignResourcesActions(game,
                diceCastActions);
        game = actionExecutorService.assignResources(game, assignResourcesActions);
        game.getLastTurn().setAssignResourcesActions(assignResourcesActions);
        game.getLastTurn().setDiceCastActions(diceCastActions);
        return game;
    }

    private Game moveCardsWithAI(Game game) {
        List<MoveCardAction> moveCardActions = moveCardsAIService.getMoveCardsActions(game);
        game = actionExecutorService.moveCards(game, moveCardActions);
        game.getLastTurn().setMoveCardActions(moveCardActions);
        return game;
    }

    private Game drawFromBacklogWithAI(Game game) {
        List<DrawFromBacklogAction> drawFromBacklogActions = drawFromBacklogAIService.getDrawFromBacklogActions(game);
        game = actionExecutorService.drawFromBacklog(game, drawFromBacklogActions);
        game.getLastTurn().setDrawFromBacklogActions(drawFromBacklogActions);
        return game;
    }
}

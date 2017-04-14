package fi.aalto.ekanban.services.turnplayers;

import java.util.List;

import fi.aalto.ekanban.models.DrawFromBacklogAction;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.services.ActionExecutorService;
import fi.aalto.ekanban.services.ai.drawfrombacklog.DrawFromBacklogAIService;
import fi.aalto.ekanban.services.ai.movecards.MoveCardsAIService;


@FunctionalInterface
public interface TurnPlayer {
    Game playTurn(Game game, Turn turn);

    default Game moveCardsWithAI(MoveCardsAIService moveCardsAIService,
                                 ActionExecutorService actionExecutorService, Game game) {
        List<MoveCardAction> moveCardActions = moveCardsAIService.getMoveCardsActions(game);
        game = actionExecutorService.moveCards(game, moveCardActions);
        game.getLastTurn().setMoveCardActions(moveCardActions);
        return game;
    }

    default Game drawFromBacklogWithAI(DrawFromBacklogAIService drawFromBacklogAIService,
                                       ActionExecutorService actionExecutorService, Game game) {
        List<DrawFromBacklogAction> drawFromBacklogActions = drawFromBacklogAIService.getDrawFromBacklogActions(game);
        game = actionExecutorService.drawFromBacklog(game, drawFromBacklogActions);
        game.getLastTurn().setDrawFromBacklogActions(drawFromBacklogActions);
        return game;
    }
}

package fi.aalto.ekanban.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.DrawFromBacklogAction;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

@Service
public class PlayerService {

    public Game playTurn(Game game, Turn turn) {
        return null;
    }

    public static Game adjustWipLimits(Game game, AdjustWipLimitsAction adjustWipLimitsAction) {
        if (adjustWipLimitsAction != null && game.getBoard() != null && game.getBoard().getPhases() != null) {
            game.getBoard().getPhases()
                    .forEach(phase -> {
                        String key = phase.getId();
                        Integer newWipLimitForPhaseOrUseOldIfNotGiven =
                                adjustWipLimitsAction.getPhaseWipLimits().getOrDefault(key, phase.getWipLimit());
                        phase.setWipLimit(newWipLimitForPhaseOrUseOldIfNotGiven);
                    });
        }
        return game;
    }

    public Game assignResources(Game game) {
        return null;
    }

    public static Game moveCards(Game game, List<MoveCardAction> moveCardActions) {
        return game;
    }

    public static Game drawFromBacklog(Game game, List<DrawFromBacklogAction> drawActions) {
        Phase firstPhase = game.getBoard().getPhases().get(0);

        drawActions.forEach(drawAction -> {
            if (isValidDrawFromBacklogAction(firstPhase, drawAction)) {
                List<Card> backlogDeck = game.getBoard().getBacklogDeck();
                Card cardToDraw = backlogDeck.get(0);
                Boolean cardSuccessfullyRemovedFromDeck = backlogDeck.remove(cardToDraw);
                if (cardSuccessfullyRemovedFromDeck) {
                    Column columnWhereCardIsToBeSet = firstPhase.getColumns().get(0);
                    columnWhereCardIsToBeSet.getCards().add(drawAction.getIndexToPlaceCardAt(), cardToDraw);
                    cardToDraw.setDayStarted(game.getCurrentDay());
                }
            }
        });

        return game;
    }

    private static boolean isValidDrawFromBacklogAction(Phase firstPhase, DrawFromBacklogAction drawAction) {
        return !firstPhase.isFullWip()
                && drawAction.getIndexToPlaceCardAt() >= 0
                && drawAction.getIndexToPlaceCardAt() <= firstPhase.getColumns().get(0).getCards().size();
    }

}

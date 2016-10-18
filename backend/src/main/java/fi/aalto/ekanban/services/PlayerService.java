package fi.aalto.ekanban.services;

import org.springframework.stereotype.Service;

import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;

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

    public Game moveCards(Game game) {
        return null;
    }

    public Game drawFromBacklog(Game game) {
        return null;
    }

}

package fi.aalto.ekanban.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.*;
import fi.aalto.ekanban.models.db.games.*;
import fi.aalto.ekanban.services.turnplayers.NormalTurnPlayerService;
import fi.aalto.ekanban.services.turnplayers.TurnPlayer;

@Service
public class PlayerService {

    private NormalTurnPlayerService normalTurnPlayerService;

    @Autowired
    public PlayerService(NormalTurnPlayerService normalTurnPlayerService) {
        this.normalTurnPlayerService = normalTurnPlayerService;
    }

    public Game playTurn(Game game, Turn turn) {
        if (game != null && game.isValid() && turn != null) {
            TurnPlayer turnPlayer = getTurnPlayer(game);
            game = turnPlayer.playTurn(game, turn);
        }
        return game;
    }

    private TurnPlayer getTurnPlayer(Game game) {
        if (game.getDifficultyLevel() == GameDifficulty.NORMAL) {
            return normalTurnPlayerService;
        }
        else {
            throw new UnsupportedOperationException("Just the normal version is supported for now");
        }
    }
}

package fi.aalto.ekanban.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.*;
import fi.aalto.ekanban.models.db.games.*;
import fi.aalto.ekanban.services.turnplayers.MediumTurnPlayerService;
import fi.aalto.ekanban.services.turnplayers.NormalTurnPlayerService;
import fi.aalto.ekanban.services.turnplayers.TurnPlayer;

@Service
public class PlayerService {

    private NormalTurnPlayerService normalTurnPlayerService;
    private MediumTurnPlayerService mediumTurnPlayerService;
    private CFDCalculatorService cfdCalculatorService;

    @Autowired
    public PlayerService(NormalTurnPlayerService normalTurnPlayerService, MediumTurnPlayerService mediumTurnPlayerService,
                         CFDCalculatorService cfdCalculatorService) {
        this.normalTurnPlayerService = normalTurnPlayerService;
        this.mediumTurnPlayerService = mediumTurnPlayerService;
        this.cfdCalculatorService = cfdCalculatorService;
    }

    public Game playTurn(Game game, Turn turn) {
        if (game != null && game.isValid() && turn != null) {
            TurnPlayer turnPlayer = getTurnPlayer(game);
            game.setCurrentDay(game.getCurrentDay() + 1);
            game = turnPlayer.playTurn(game, turn);
            game = endGameIfNeeded(game);
            game = cfdCalculatorService.calculateCFDForCurrentDay(game);
        }
        return game;
    }

    private Game endGameIfNeeded(Game game) {
        if (!game.getHasEnded() && game.canBeEnded()) {
            game.setHasEnded(true);
        }
        return game;
    }

    private TurnPlayer getTurnPlayer(Game game) {
        if (game.getDifficultyLevel() == GameDifficulty.NORMAL) {
            return normalTurnPlayerService;
        }
        else if (game.getDifficultyLevel() == GameDifficulty.MEDIUM) {
            return mediumTurnPlayerService;
        }
        else {
            // the current advanced version doesn't hold anything new over mediumTurnPlayer
            return mediumTurnPlayerService;
        }
    }
}

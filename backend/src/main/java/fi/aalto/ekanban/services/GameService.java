package fi.aalto.ekanban.services;

import fi.aalto.ekanban.exceptions.GameNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.*;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private GameInitService gameInitService;
    private PlayerService playerService;
    private GameOptionService gameOptionService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    public GameService(GameInitService gameInitService,
                       PlayerService playerService,
                       GameOptionService gameOptionService) {
        this.gameInitService = gameInitService;
        this.playerService = playerService;
        this.gameOptionService = gameOptionService;
    }

    @Transactional
    public Game startGame(String playerName, GameDifficulty gameDifficulty) {
        Game newGame = gameInitService.getInitializedGame(gameDifficulty, playerName);
        Game createdGame = gameRepository.save(newGame);
        return createdGame;
    }

    @Transactional
    public Game playTurn(String gameId, Turn turn) {
        Game game = gameRepository.findOne(gameId);
        if (game == null) {
            throw new GameNotFoundException();
        }
        Game playedGame = playerService.playTurn(game, turn);
        gameRepository.save(playedGame);
        return playedGame;
    }

}

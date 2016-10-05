package fi.aalto.ekanban.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.models.games.Game;
import fi.aalto.ekanban.models.games.Turn;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private GameInitService gameInitService;
    private PlayerService playerService;
    private GameOptionService gameOptionService;

    @Autowired
    public GameService(GameInitService gameInitService,
                       PlayerService playerService,
                       GameOptionService gameOptionService) {
        this.gameInitService = gameInitService;
        this.playerService = playerService;
        this.gameOptionService = gameOptionService;
    }

    public Game startGame() {
        return null;
    }

    public Game playTurn(String gameId, Turn turn) {
        return null;
    }

}

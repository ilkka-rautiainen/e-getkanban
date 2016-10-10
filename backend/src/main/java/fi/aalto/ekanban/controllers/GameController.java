package fi.aalto.ekanban.controllers;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.services.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fi.aalto.ekanban.models.db.games.Game;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameService gameService;

    @RequestMapping(method = RequestMethod.POST)
    public Game startGame(@RequestParam("playerName") String playerName,
                          @RequestParam("difficultyLevel") GameDifficulty gameDifficulty) {
        return gameService.startGame(playerName, gameDifficulty);
    }

}

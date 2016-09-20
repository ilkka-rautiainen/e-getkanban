package fi.aalto.ekanban.controllers;

import fi.aalto.ekanban.models.Game;
import fi.aalto.ekanban.repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/games")
public class GameController {

    @Autowired
    GameRepository gameRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<Game> getGames() {
        return gameRepository.findAll();
    }

}
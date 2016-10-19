package fi.aalto.ekanban.services;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.models.db.games.Game;

@Service
public class GameInitService {

    public Game getInitializedGame(GameDifficulty gameDifficulty) {
        Game blankGame = GameBuilder.aGame()
                .withCurrentDay(1).build();
        //modify the blankGame with different difficulty options in medium & advanced
        return blankGame;
    }

}

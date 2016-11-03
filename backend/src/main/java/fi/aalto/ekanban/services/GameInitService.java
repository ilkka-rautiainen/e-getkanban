package fi.aalto.ekanban.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.repositories.*;

@Service
public class GameInitService {

    @Autowired
    BaseCardRepository baseCardRepository;

    @Autowired
    PhaseRepository phaseRepository;

    public Game getInitializedGame(GameDifficulty gameDifficulty, String playerName) {
        return GameBuilder.aGame()
                .withNormalDifficultyDefaults(playerName, baseCardRepository, phaseRepository)
                .build();
    }

}

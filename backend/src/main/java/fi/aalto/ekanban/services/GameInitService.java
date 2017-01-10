package fi.aalto.ekanban.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.models.db.gameconfigurations.DifficultyConfiguration;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

@Service
public class GameInitService {

    @Autowired
    BaseCardRepository baseCardRepository;

    @Autowired
    PhaseRepository phaseRepository;

    public Game getInitializedGame(DifficultyConfiguration difficultyConfiguration, String playerName) {
        if (difficultyConfiguration.isNormal()) {
            return GameBuilder.aGame()
                    .withNormalDifficultyDefaults(difficultyConfiguration,
                            playerName, baseCardRepository, phaseRepository)
                    .build();
        }
        else {
            throw new UnsupportedOperationException("Just the normal version is supported for now");
        }
    }

}

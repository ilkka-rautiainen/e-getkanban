package fi.aalto.ekanban.services;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.exceptions.GameInitException;
import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;
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
        Game gameForDiffConfig = null;
        if (difficultyConfiguration.isNormal()) {
            gameForDiffConfig = GameBuilder.aGame()
                    .withNormalDifficultyDefaults(difficultyConfiguration,
                            playerName, baseCardRepository, phaseRepository)
                    .build();
        }
        else if (difficultyConfiguration.isMedium()) {
            gameForDiffConfig = GameBuilder.aGame()
                    .withNormalDifficultyDefaults(difficultyConfiguration,
                            playerName, baseCardRepository, phaseRepository)
                    .withDifficultyLevel(GameDifficulty.MEDIUM)
                    .build();
        }
        else if (difficultyConfiguration.isAdvanced()) {
            Object gameWithGameOptionChanges = GameBuilder.aGame()
                    .withNormalDifficultyDefaults(difficultyConfiguration,
                            playerName, baseCardRepository, phaseRepository)
                    .withDifficultyLevel(GameDifficulty.ADVANCED)
                    .build();
            for (int i = 0; i < difficultyConfiguration.getInitialGameOptionChanges().size(); i++) {
                GameOptionChange gameOptionChange = difficultyConfiguration.getInitialGameOptionChanges().get(i);
                String methodName = gameOptionChange.getMethodName();
                try {
                    Class parameterCls = Class.forName(gameOptionChange.getParameters());
                    Method methodToInvoke = GameOptionService.class.getMethod(methodName, parameterCls);
                    gameWithGameOptionChanges = methodToInvoke.invoke(new GameOptionService(), gameWithGameOptionChanges);
                } catch (Exception e) {
                    throw new GameInitException();
                }
            }
            gameForDiffConfig = (Game) gameWithGameOptionChanges;
        }
        return gameForDiffConfig;
    }

}

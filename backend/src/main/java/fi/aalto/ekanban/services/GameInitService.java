package fi.aalto.ekanban.services;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE_ID;

import java.lang.reflect.Method;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.exceptions.GameInitException;
import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;
import fi.aalto.ekanban.models.db.phases.Column;
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
                    Class cls = Class.forName(gameOptionChange.getParameters());
                    Method methodToInvoke = this.getClass().getMethod(methodName, cls);
                    gameWithGameOptionChanges = methodToInvoke.invoke(this, gameWithGameOptionChanges);
                } catch (Exception e) {
                    throw new GameInitException();
                }
            }
            gameForDiffConfig = (Game) gameWithGameOptionChanges;
        }
        return gameForDiffConfig;
    }

    public Game enableCardThreeDayMoveCycle(Game gameToChange) {
        Column inProgressColumnOfAnalysis = gameToChange.getBoard().getPhaseWithId(ANALYSIS_PHASE_ID).getFirstColumn();
        Column firstColumnOfDeployed = gameToChange.getBoard().getPhaseWithId(DEPLOYED_PHASE_ID).getFirstColumn();
        inProgressColumnOfAnalysis.setDayMultiplierToEnter(3);
        firstColumnOfDeployed.setDayMultiplierToEnter(3);
        return gameToChange;
    }

}

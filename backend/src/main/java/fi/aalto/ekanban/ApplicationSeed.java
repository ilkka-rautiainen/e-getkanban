package fi.aalto.ekanban;

import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.repositories.DifficultyConfigurationRepository;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

/*
 * Seeds data automatically in the process of spring IOC
 */
@Component
public class ApplicationSeed {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationSeed.class);

    @Autowired
    public ApplicationSeed(PhaseRepository phaseRepository, BaseCardRepository baseCardRepository,
                           DifficultyConfigurationRepository difficultyConfigurationRepository) {
        try {
            seedPhases(phaseRepository);
            seedBaseCards(baseCardRepository);
            seedDifficultyConfigurations(difficultyConfigurationRepository);
        } catch (MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            throw ex;
        }

    }

    private void seedPhases(PhaseRepository phaseRepository) {
        PhasesBuilder.aSetOfPhases()
                .withPhasesForAllGameDifficulties()
                .createIfNotCreated(phaseRepository);
    }

    private void seedBaseCards(BaseCardRepository baseCardRepository) {
        BaseCardsBuilder.aSetOfCards()
                .withAllBaseCardsWithMockPoints()
                .createIfNotCreated(baseCardRepository);
    }

    private void seedDifficultyConfigurations(DifficultyConfigurationRepository difficultyConfigurationRepository) {
        DifficultyConfigurationsBuilder.aSetOfDifficultyConfigurations()
                .withAllDifficultyConfigurations()
                .createIfNotCreated(difficultyConfigurationRepository);
    }

}

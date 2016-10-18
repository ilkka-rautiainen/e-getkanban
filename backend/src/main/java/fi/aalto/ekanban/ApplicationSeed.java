package fi.aalto.ekanban;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE;
import static fi.aalto.ekanban.ApplicationConstants.TEST_PHASE;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE;

import java.util.Arrays;
import java.util.List;

import com.mongodb.MongoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.aalto.ekanban.builders.BaseCardBuilder;
import fi.aalto.ekanban.builders.CardPhasePointBuilder;
import fi.aalto.ekanban.builders.PhaseBuilder;
import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;


/*
Idea is to run the component for data seeding automatically in the process of spring IOC
 */
@Component
public class ApplicationSeed {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationSeed.class);

    @Autowired
    public ApplicationSeed(PhaseRepository phaseRepository, BaseCardRepository baseCardRepository) {
        try {
            seedPhases(phaseRepository);
            seedBaseCards(baseCardRepository, phaseRepository);
        } catch (MongoException ex) {
            logger.error(ex.getLocalizedMessage());
            throw ex;
        }

    }

    private void seedPhases(PhaseRepository phaseRepository) {
        if (phaseRepository.findByName(ANALYSIS_PHASE) == null)
            PhaseBuilder.aPhase().analysis().create(phaseRepository);
        if (phaseRepository.findByName(DEVELOPMENT_PHASE) == null)
            PhaseBuilder.aPhase().development().create(phaseRepository);
        if (phaseRepository.findByName(TEST_PHASE) == null)
            PhaseBuilder.aPhase().test().create(phaseRepository);
        if (phaseRepository.findByName(DEPLOYED_PHASE) == null)
            PhaseBuilder.aPhase().deployed().create(phaseRepository);
    }

    private void seedBaseCards(BaseCardRepository baseCardRepository, PhaseRepository phaseRepository) {
        if (baseCardRepository.findAll().isEmpty()) {
            Phase analysisPhase = phaseRepository.findByName(ANALYSIS_PHASE);
            Phase developmentPhase = phaseRepository.findByName(DEVELOPMENT_PHASE);
            Phase testPhase = phaseRepository.findByName(TEST_PHASE);

            CardPhasePoint analysisCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                    .withTotalPoints(5)
                    .withPhaseId(analysisPhase.getId())
                    .build();
            CardPhasePoint developmentCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                    .withTotalPoints(5)
                    .withPhaseId(developmentPhase.getId())
                    .build();
            CardPhasePoint testCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                    .withTotalPoints(5)
                    .withPhaseId(testPhase.getId())
                    .build();
            List<CardPhasePoint> cardPhasePoints = Arrays.asList(analysisCardPoints, developmentCardPoints, testCardPoints);

            for (Integer i = 0; i < 5; i++) {
                BaseCardBuilder.aBaseCard()
                        .withFinancialValue(FinancialValue.HIGH)
                        .withCardPhasePoints(cardPhasePoints)
                        .withSubscribesWhenDeployed("1")
                        .create(baseCardRepository);
                BaseCardBuilder.aBaseCard()
                        .withFinancialValue(FinancialValue.MED)
                        .withCardPhasePoints(cardPhasePoints)
                        .withSubscribesWhenDeployed("1")
                        .create(baseCardRepository);
                BaseCardBuilder.aBaseCard()
                        .withFinancialValue(FinancialValue.LOW)
                        .withCardPhasePoints(cardPhasePoints)
                        .withSubscribesWhenDeployed("1")
                        .create(baseCardRepository);
            }
        }
    }

}

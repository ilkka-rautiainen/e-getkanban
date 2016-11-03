package fi.aalto.ekanban.builders;

import static fi.aalto.ekanban.ApplicationConstants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.repositories.PhaseRepository;

public class PhasesBuilder {

    private List<PhaseBuilder> phaseBuilders;

    public static PhasesBuilder aSetOfPhases() {
        PhasesBuilder builder = new PhasesBuilder();
        builder.phaseBuilders = new ArrayList<>();
        return builder;
    }

    public static List<Phase> normalDifficultyPhases(PhaseRepository phaseRepository) {
        return Arrays.asList(
                phaseRepository.findOne(ANALYSIS_PHASE_ID),
                phaseRepository.findOne(DEVELOPMENT_PHASE_ID),
                phaseRepository.findOne(TEST_PHASE_ID),
                phaseRepository.findOne(DEPLOYED_PHASE_ID));
    }

    public PhasesBuilder withPhasesForAllGameDifficulties() {
        phaseBuilders.add(PhaseBuilder.aPhase().withAnalysisDefaults());
        phaseBuilders.add(PhaseBuilder.aPhase().withDevelopmentDefaults());
        phaseBuilders.add(PhaseBuilder.aPhase().withTestDefaults());
        phaseBuilders.add(PhaseBuilder.aPhase().withDeployedDefaults());
        return this;
    }

    public PhasesBuilder withNormalDifficultyMockPhases() {
        phaseBuilders.add(PhaseBuilder.aPhase().withAnalysisDefaults());
        phaseBuilders.add(PhaseBuilder.aPhase().withDevelopmentDefaults());
        phaseBuilders.add(PhaseBuilder.aPhase().withTestDefaults());
        phaseBuilders.add(PhaseBuilder.aPhase().withDeployedDefaults());
        return this;
    }

    public List<Phase> build() {
        return phaseBuilders.stream()
                .map(PhaseBuilder::build)
                .collect(Collectors.toList());
    }

    public List<Phase> createIfNotCreated(PhaseRepository phaseRepository) {
        return phaseBuilders.stream()
                .map(phaseBuilder -> phaseBuilder.createIfNotCreated(phaseRepository))
                .collect(Collectors.toList());
    }
}

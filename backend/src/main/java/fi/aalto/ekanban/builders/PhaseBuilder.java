package fi.aalto.ekanban.builders;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.TEST_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE_ID;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.repositories.PhaseRepository;

public final class PhaseBuilder {
    private String id;
    private List<Column> columns;
    private Integer wipLimit;
    private String name;
    private String shortName;
    private Boolean isWorkPhase;

    private PhaseBuilder() {}

    public static PhaseBuilder aPhase() {
        return new PhaseBuilder();
    }

    public PhaseBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public PhaseBuilder withColumns(List<Column> columns) {
        this.columns = columns;
        return this;
    }

    public PhaseBuilder withWipLimit(Integer wipLimit) {
        this.wipLimit = wipLimit;
        return this;
    }

    public PhaseBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public PhaseBuilder withShortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    public PhaseBuilder withAnalysisDefaults(Phase nextPhase) {
        this.id = ANALYSIS_PHASE_ID;
        this.wipLimit = 2;
        this.columns = Arrays.asList(
                ColumnBuilder.aColumn().inProgress().build(),
                ColumnBuilder.aColumn().waitingForNext(nextPhase).build());
        this.name = "Analysis";
        this.shortName = "An";
        this.isWorkPhase = true;
        return this;
    }

    public PhaseBuilder withDevelopmentDefaults(Phase nextPhase) {
        this.id = DEVELOPMENT_PHASE_ID;
        this.wipLimit = 4;
        this.columns = Arrays.asList(
                ColumnBuilder.aColumn().inProgress().build(),
                ColumnBuilder.aColumn().waitingForNext(nextPhase).build()
        );
        this.name = "Development";
        this.shortName = "Dev";
        this.isWorkPhase = true;
        return this;
    }

    public PhaseBuilder withTestDefaults() {
        this.id = TEST_PHASE_ID;
        this.wipLimit = 3;
        this.columns = Arrays.asList(ColumnBuilder.aColumn().withCards(new ArrayList<>()).build());
        this.name = "Test";
        this.shortName = "Test";
        this.isWorkPhase = true;
        return this;
    }

    public PhaseBuilder withDeployedDefaults() {
        this.id = DEPLOYED_PHASE_ID;
        this.columns = Arrays.asList(ColumnBuilder.aColumn().withCards(new ArrayList<>()).build());
        this.name = "Deployed";
        this.shortName = "Depl";
        this.isWorkPhase = false;
        return this;
    }

    public Phase createIfNotCreated(PhaseRepository repository) {
        Phase phase = repository.findOne(id);
        if (phase != null) {
            return phase;
        }
        else {
            phase = build();
            return repository.save(phase);
        }
    }

    public Phase build() {
        Phase phase = new Phase();
        phase.setId(id);
        phase.setColumns(columns);
        phase.setWipLimit(wipLimit);
        phase.setName(name);
        phase.setShortName(shortName);
        phase.setIsWorkPhase(isWorkPhase);
        return phase;
    }
}

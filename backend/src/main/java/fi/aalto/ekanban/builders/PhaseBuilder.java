package fi.aalto.ekanban.builders;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE;
import static fi.aalto.ekanban.ApplicationConstants.TEST_PHASE;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE;

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

    public PhaseBuilder analysis() {
        this.wipLimit = 2;
        this.columns = Arrays.asList(
                ColumnBuilder.aColumn().inProgress().build(),
                ColumnBuilder.aColumn().done().build());
        this.name = ANALYSIS_PHASE;
        return this;
    }

    public PhaseBuilder development() {
        this.wipLimit = 4;
        this.columns = Arrays.asList(
                ColumnBuilder.aColumn().inProgress().build(),
                ColumnBuilder.aColumn().done().build()
        );
        this.name = DEVELOPMENT_PHASE;
        return this;
    }

    public PhaseBuilder test() {
        this.wipLimit = 3;
        this.columns = Arrays.asList(ColumnBuilder.aColumn().withCards(new ArrayList<>()).build());
        this.name = TEST_PHASE;
        return this;
    }

    public PhaseBuilder deployed() {
        this.columns = Arrays.asList(ColumnBuilder.aColumn().withCards(new ArrayList<>()).build());
        this.name = DEPLOYED_PHASE;
        return this;
    }

    public Phase create(PhaseRepository repository) {
        Phase phase = new Phase();
        phase.setId(id);
        phase.setColumns(columns);
        phase.setWipLimit(wipLimit);
        phase.setName(name);
        return repository.save(phase);
    }

    public Phase build() {
        Phase phase = new Phase();
        phase.setId(id);
        phase.setColumns(columns);
        phase.setWipLimit(wipLimit);
        phase.setName(name);
        return phase;
    }
}

package fi.aalto.ekanban.builders;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import fi.aalto.ekanban.models.db.games.Column;
import fi.aalto.ekanban.models.db.games.Phase;

public final class PhaseBuilder {
    private String id;
    private List<Column> columns;
    private Integer wipLimit;
    private String name;

    private PhaseBuilder() {
        this.id = ObjectId.get().toString();
    }

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
        this.name = "Analysis";
        return this;
    }

    public PhaseBuilder development() {
        this.wipLimit = 4;
        this.columns = Arrays.asList(
                ColumnBuilder.aColumn().inProgress().build(),
                ColumnBuilder.aColumn().done().build()
        );
        this.name = "Development";
        return this;
    }

    public PhaseBuilder test() {
        this.wipLimit = 3;
        this.columns = Arrays.asList(ColumnBuilder.aColumn().build());
        this.name = "Test";
        return this;
    }

    public PhaseBuilder deployed() {
        this.columns = Arrays.asList(ColumnBuilder.aColumn().build());
        this.name = "Deployed";
        return this;
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

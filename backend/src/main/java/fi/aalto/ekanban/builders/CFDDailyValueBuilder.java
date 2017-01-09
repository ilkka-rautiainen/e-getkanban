package fi.aalto.ekanban.builders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import fi.aalto.ekanban.models.CFDDailyValue;
import fi.aalto.ekanban.models.db.phases.Phase;

public final class CFDDailyValueBuilder {
    private Integer day;
    private Integer enteredBoard;
    // phase id -> daily value
    private Map<String, Integer> phaseValues;

    private CFDDailyValueBuilder() {
        this.phaseValues = new HashMap<>();
    }

    public static CFDDailyValueBuilder aCFDDailyValue() {
        return new CFDDailyValueBuilder();
    }

    public CFDDailyValueBuilder withDay(Integer day) {
        this.day = day;
        return this;
    }

    public CFDDailyValueBuilder withEnteredBoard(Integer enteredBoard) {
        this.enteredBoard = enteredBoard;
        return this;
    }

    public CFDDailyValueBuilder withPhaseValues(Map<String, Integer> phaseValues) {
        this.phaseValues = phaseValues;
        return this;
    }

    public CFDDailyValueBuilder withZeroValuesBasedOnPhases(List<Phase> phases) {
        this.enteredBoard = 0;
        phases.stream()
                .filter(phase -> phase.getTrackLinePlace() != null)
                .collect(Collectors.toList())
                .forEach(phase -> this.phaseValues.put(phase.getId(), 0));
        return this;
    }

    public CFDDailyValue build() {
        CFDDailyValue cFDDailyValue = new CFDDailyValue();
        cFDDailyValue.setDay(day);
        cFDDailyValue.setEnteredBoard(enteredBoard);
        cFDDailyValue.setPhaseValues(phaseValues);
        return cFDDailyValue;
    }
}

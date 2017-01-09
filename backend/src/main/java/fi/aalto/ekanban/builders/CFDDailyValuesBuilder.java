package fi.aalto.ekanban.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fi.aalto.ekanban.models.CFDDailyValue;
import fi.aalto.ekanban.models.db.phases.Phase;

public class CFDDailyValuesBuilder {

    private List<CFDDailyValueBuilder> cfdDailyValuesBuilders;

    public static CFDDailyValuesBuilder aSetOfCfdDailyValues() {
        CFDDailyValuesBuilder builder = new CFDDailyValuesBuilder();
        builder.cfdDailyValuesBuilders = new ArrayList<>();
        return builder;
    }

    public CFDDailyValuesBuilder withOneDailyValueWithZerosBasedOnPhases(List<Phase> phases) {
        cfdDailyValuesBuilders.add(CFDDailyValueBuilder.aCFDDailyValue()
                .withDay(0)
                .withZeroValuesBasedOnPhases(phases));
        return this;
    }

    public CFDDailyValuesBuilder withValuesWithZerosBasedOnPhasesUntilDay(List<Phase> phases, Integer day) {
        for (int i = 0; i <= day; i++) {
            withOneDailyValueWithZerosBasedOnPhases(phases);
        }
        return this;
    }

    public List<CFDDailyValue> build() {
        return cfdDailyValuesBuilders.stream()
                .map(CFDDailyValueBuilder::build)
                .collect(Collectors.toList());
    }
}

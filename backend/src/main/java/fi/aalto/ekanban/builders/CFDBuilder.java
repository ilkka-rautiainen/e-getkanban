package fi.aalto.ekanban.builders;

import java.util.ArrayList;
import java.util.List;

import fi.aalto.ekanban.models.CFD;
import fi.aalto.ekanban.models.CFDDailyValue;

public final class CFDBuilder {
    private List<CFDDailyValue> cfdDailyValues;

    private CFDBuilder() {
        this.cfdDailyValues = new ArrayList<>();
    }

    public static CFDBuilder aCFD() {
        return new CFDBuilder();
    }

    public CFDBuilder withCfdDailyValues(List<CFDDailyValue> cfdDailyValues) {
        this.cfdDailyValues = cfdDailyValues;
        return this;
    }

    public CFD build() {
        CFD cFD = new CFD();
        cFD.setCfdDailyValues(cfdDailyValues);
        return cFD;
    }
}

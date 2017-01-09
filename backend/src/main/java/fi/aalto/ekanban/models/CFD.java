package fi.aalto.ekanban.models;

import java.util.List;

public class CFD {

    private List<CFDDailyValue> cfdDailyValues;

    public List<CFDDailyValue> getCfdDailyValues() {
        return cfdDailyValues;
    }

    public void setCfdDailyValues(List<CFDDailyValue> cfdDailyValues) {
        this.cfdDailyValues = cfdDailyValues;
    }

    public void addCfdDailyValue(CFDDailyValue dailyValue) {
        this.cfdDailyValues.add(dailyValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof CFD)) return false;

        CFD cfd = (CFD) o;

        return cfdDailyValues != null ? cfdDailyValues.equals(cfd.cfdDailyValues) : cfd.cfdDailyValues == null;
    }

    @Override
    public int hashCode() {
        return cfdDailyValues != null ? cfdDailyValues.hashCode() : 0;
    }
}

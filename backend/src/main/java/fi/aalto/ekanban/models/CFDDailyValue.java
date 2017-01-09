package fi.aalto.ekanban.models;

import java.util.Map;

public class CFDDailyValue {
    private Integer day;
    private Integer enteredBoard;
    // phase id -> daily value
    private Map<String, Integer> phaseValues;

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getEnteredBoard() {
        return enteredBoard;
    }

    public void setEnteredBoard(Integer enteredBoard) {
        this.enteredBoard = enteredBoard;
    }

    public Map<String, Integer> getPhaseValues() {
        return phaseValues;
    }

    public void setPhaseValues(Map<String, Integer> phaseValues) {
        this.phaseValues = phaseValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof CFDDailyValue)) return false;

        CFDDailyValue cfdDailyValue = (CFDDailyValue) o;

        if (day != null ? !day.equals(cfdDailyValue.day) : cfdDailyValue.day != null) return false;
        if (enteredBoard != null ? !enteredBoard.equals(cfdDailyValue.enteredBoard) : cfdDailyValue.enteredBoard != null) return false;
        return phaseValues != null ? phaseValues.equals(cfdDailyValue.phaseValues) : cfdDailyValue.phaseValues == null;
    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (enteredBoard != null ? enteredBoard.hashCode() : 0);
        result = 31 * result + (phaseValues != null ? phaseValues.hashCode() : 0);
        return result;
    }
}

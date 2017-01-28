package fi.aalto.ekanban.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class DiceCastAction {
    private String phaseId;
    private List<Integer> diceValues;

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public List<Integer> getDiceValues() {
        return diceValues;
    }

    public void setDiceValues(List<Integer> diceValues) {
        this.diceValues = diceValues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof DiceCastAction)) return false;

        DiceCastAction action = (DiceCastAction) o;

        if (phaseId != null ? !phaseId.equals(action.phaseId) : action.phaseId != null) return false;
        return diceValues != null ? diceValues.equals(action.diceValues) : action.diceValues == null;

    }

    @Override
    public int hashCode() {
        int result = phaseId != null ? phaseId.hashCode() : 0;
        result = 31 * result + (diceValues != null ? diceValues.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    public Integer getTotalDiceValue() {
        return getDiceValues()
                .stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}

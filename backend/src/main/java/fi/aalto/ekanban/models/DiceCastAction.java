package fi.aalto.ekanban.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import fi.aalto.ekanban.builders.Die;

public class DiceCastAction {
    private String phaseId;
    private List<Die> dice;

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public List<Die> getDice() {
        return dice;
    }

    public void setDice(List<Die> Dice) {
        this.dice = Dice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof DiceCastAction)) return false;

        DiceCastAction action = (DiceCastAction) o;

        if (phaseId != null ? !phaseId.equals(action.phaseId) : action.phaseId != null) return false;
        return dice != null ? dice.equals(action.dice) : action.dice == null;

    }

    @Override
    public int hashCode() {
        int result = phaseId != null ? phaseId.hashCode() : 0;
        result = 31 * result + (dice != null ? dice.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    public Integer getTotalDiceValue() {
        return getDice()
                .stream()
                .mapToInt(die -> die.getPrimaryValue())
                .sum();
    }
}

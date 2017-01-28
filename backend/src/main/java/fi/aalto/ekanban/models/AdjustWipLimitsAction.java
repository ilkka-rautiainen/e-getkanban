package fi.aalto.ekanban.models;

import java.util.Map;

import fi.aalto.ekanban.validators.CheckWipLimits;

public class AdjustWipLimitsAction {

    // Phase id -> new wip-limit
    @CheckWipLimits
    private Map<String, Integer> phaseWipLimits;

    public Map<String, Integer> getPhaseWipLimits() {
        return phaseWipLimits;
    }

    public void setPhaseWipLimits(Map<String, Integer> phaseWipLimits) {
        this.phaseWipLimits = phaseWipLimits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof AdjustWipLimitsAction)) return false;

        AdjustWipLimitsAction action = (AdjustWipLimitsAction) o;

        return phaseWipLimits != null ? phaseWipLimits.equals(action.phaseWipLimits) : action.phaseWipLimits == null;

    }

    @Override
    public int hashCode() {
        int result = phaseWipLimits != null ? phaseWipLimits.hashCode() : 0;
        return result;
    }

}

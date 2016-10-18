package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.AdjustWipLimitsAction;

import java.util.Map;

public final class AdjustWipLimitsActionBuilder {
    private Map<String, Integer> phaseWipLimits;

    private AdjustWipLimitsActionBuilder() {
    }

    public static AdjustWipLimitsActionBuilder anAdjustWipLimitsAction() {
        return new AdjustWipLimitsActionBuilder();
    }

    public AdjustWipLimitsActionBuilder withPhaseWipLimits(Map<String, Integer> phaseWipLimits) {
        this.phaseWipLimits = phaseWipLimits;
        return this;
    }

    public AdjustWipLimitsAction build() {
        AdjustWipLimitsAction adjustWipLimitsAction = new AdjustWipLimitsAction();
        adjustWipLimitsAction.setPhaseWipLimits(phaseWipLimits);
        return adjustWipLimitsAction;
    }
}

package fi.aalto.ekanban.builders;

import java.util.List;

import fi.aalto.ekanban.models.DiceCastAction;

public final class DiceCastActionBuilder {
    private String phaseId;
    private List<Integer> diceValues;

    private DiceCastActionBuilder() {
    }

    public static DiceCastActionBuilder aDiceCastAction() {
        return new DiceCastActionBuilder();
    }

    public DiceCastActionBuilder withPhaseId(String phaseId) {
        this.phaseId = phaseId;
        return this;
    }

    public DiceCastActionBuilder withDiceValues(List<Integer> diceValues) {
        this.diceValues = diceValues;
        return this;
    }

    public DiceCastAction build() {
        DiceCastAction diceCastAction = new DiceCastAction();
        diceCastAction.setPhaseId(phaseId);
        diceCastAction.setDiceValues(diceValues);
        return diceCastAction;
    }
}

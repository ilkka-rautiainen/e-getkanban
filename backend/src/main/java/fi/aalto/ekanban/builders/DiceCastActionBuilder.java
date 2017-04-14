package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.DiceCastAction;

import java.util.List;

public final class DiceCastActionBuilder {
    private String phaseId;
    private List<Die> dice;

    private DiceCastActionBuilder() {
    }

    public static DiceCastActionBuilder aDiceCastAction() {
        return new DiceCastActionBuilder();
    }

    public DiceCastActionBuilder withPhaseId(String phaseId) {
        this.phaseId = phaseId;
        return this;
    }

    public DiceCastActionBuilder withDice(List<Die> dice) {
        this.dice = dice;
        return this;
    }

    public DiceCastAction build() {
        DiceCastAction diceCastAction = new DiceCastAction();
        diceCastAction.setPhaseId(phaseId);
        diceCastAction.setDice(dice);
        return diceCastAction;
    }
}

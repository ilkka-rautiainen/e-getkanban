package fi.aalto.ekanban.services.MoveCardsAI;

import fi.aalto.ekanban.models.MoveCardAction;

import java.util.List;

public final class PhaseActionsBuilder {
    private Integer cardsLetOutOfPhase;
    private List<MoveCardAction> moveCardActions;

    private PhaseActionsBuilder() {
    }

    public static PhaseActionsBuilder aPhaseActions() {
        return new PhaseActionsBuilder();
    }

    public PhaseActionsBuilder withCardsLetOutOfPhase(Integer cardsLetOutOfPhase) {
        this.cardsLetOutOfPhase = cardsLetOutOfPhase;
        return this;
    }

    public PhaseActionsBuilder withMoveCardActions(List<MoveCardAction> moveCardActions) {
        this.moveCardActions = moveCardActions;
        return this;
    }

    public PhaseActions build() {
        PhaseActions phaseActions = new PhaseActions();
        phaseActions.setCardsLetOutOfPhase(cardsLetOutOfPhase);
        phaseActions.setMoveCardActions(moveCardActions);
        return phaseActions;
    }
}

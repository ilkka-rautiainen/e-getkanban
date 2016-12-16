package fi.aalto.ekanban.services.ai.movecards;

import java.util.List;

import fi.aalto.ekanban.models.MoveCardAction;

public class PhaseActions {
    private Integer cardsLetOutOfPhase;
    private List<MoveCardAction> moveCardActions;

    public Integer getCardsLetOutOfPhase() {
        return cardsLetOutOfPhase;
    }

    public void setCardsLetOutOfPhase(Integer cardsLetOutOfPhase) {
        this.cardsLetOutOfPhase = cardsLetOutOfPhase;
    }

    public List<MoveCardAction> getMoveCardActions() {
        return moveCardActions;
    }

    public void setMoveCardActions(List<MoveCardAction> moveCardActions) {
        this.moveCardActions = moveCardActions;
    }
}

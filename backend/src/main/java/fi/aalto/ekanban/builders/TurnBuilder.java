package fi.aalto.ekanban.builders;

import java.util.ArrayList;
import java.util.List;

import fi.aalto.ekanban.models.*;

public final class TurnBuilder {
    private AdjustWipLimitsAction adjustWipLimitsAction;
    private List<MoveCardAction> moveCardActions;
    private List<DrawFromBacklogAction> drawFromBacklogActions;

    private TurnBuilder() {
        moveCardActions = new ArrayList<>();
        drawFromBacklogActions = new ArrayList<>();
    }

    public static TurnBuilder aTurn() {
        return new TurnBuilder();
    }

    public TurnBuilder withAdjustWipLimitsAction(AdjustWipLimitsAction adjustWipLimitsAction) {
        this.adjustWipLimitsAction = adjustWipLimitsAction;
        return this;
    }

    public TurnBuilder withMoveCardActions(List<MoveCardAction> moveCardActions) {
        this.moveCardActions = moveCardActions;
        return this;
    }

    public TurnBuilder withDrawFromBacklogActions(List<DrawFromBacklogAction> drawFromBacklogActions) {
        this.drawFromBacklogActions = drawFromBacklogActions;
        return this;
    }

    public Turn build() {
        Turn turn = new Turn();
        turn.setAdjustWipLimitsAction(adjustWipLimitsAction);
        turn.setMoveCardActions(moveCardActions);
        turn.setDrawFromBacklogActions(drawFromBacklogActions);
        return turn;
    }
}

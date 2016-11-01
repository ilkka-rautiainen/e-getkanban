package fi.aalto.ekanban.models;

import javax.validation.constraints.NotNull;
import java.util.List;

public class Turn {

    private AdjustWipLimitsAction adjustWipLimitsAction;
    @NotNull
    private List<MoveCardAction> moveCardActions;
    @NotNull
    private List<DrawFromBacklogAction> drawFromBacklogActions;

    public AdjustWipLimitsAction getAdjustWipLimitsAction() {
        return adjustWipLimitsAction;
    }

    public void setAdjustWipLimitsAction(AdjustWipLimitsAction adjustWipLimitsAction) {
        this.adjustWipLimitsAction = adjustWipLimitsAction;
    }

    public List<DrawFromBacklogAction> getDrawFromBacklogActions() {
        return drawFromBacklogActions;
    }

    public void setDrawFromBacklogActions(List<DrawFromBacklogAction> drawFromBacklogActions) {
        this.drawFromBacklogActions = drawFromBacklogActions;
    }

    public List<MoveCardAction> getMoveCardActions() {
        return moveCardActions;
    }

    public void setMoveCardActions(List<MoveCardAction> moveCardActions) {
        this.moveCardActions = moveCardActions;
    }

}

package fi.aalto.ekanban.models;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class Turn {

    @Valid
    private AdjustWipLimitsAction adjustWipLimitsAction;
    @NotNull
    private List<MoveCardAction> moveCardActions;
    @NotNull
    private List<DrawFromBacklogAction> drawFromBacklogActions;
    @NotNull
    private List<AssignResourcesAction> assignResourcesActions;


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

    public List<AssignResourcesAction> getAssignResourcesActions() {
        return assignResourcesActions;
    }

    public void setAssignResourcesActions(List<AssignResourcesAction> assignResourcesActions) {
        this.assignResourcesActions = assignResourcesActions;
    }
}

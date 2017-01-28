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
    @NotNull
    private List<DiceCastAction> diceCastActions;

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

    public List<DiceCastAction> getDiceCastActions() {
        return diceCastActions;
    }

    public void setDiceCastActions(List<DiceCastAction> diceCastActions) {
        this.diceCastActions = diceCastActions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Turn)) return false;

        Turn action = (Turn) o;

        if (adjustWipLimitsAction != null ? !adjustWipLimitsAction.equals(action.adjustWipLimitsAction) : action.adjustWipLimitsAction != null) return false;
        if (moveCardActions != null ? !moveCardActions.equals(action.moveCardActions) : action.moveCardActions != null) return false;
        if (drawFromBacklogActions != null ? !drawFromBacklogActions.equals(action.drawFromBacklogActions) : action.drawFromBacklogActions != null) return false;
        if (diceCastActions != null ? !diceCastActions.equals(action.diceCastActions) : action.diceCastActions != null) return false;
        return assignResourcesActions != null ? assignResourcesActions.equals(action.assignResourcesActions) : action.assignResourcesActions == null;

    }

    @Override
    public int hashCode() {
        int result = adjustWipLimitsAction != null ? adjustWipLimitsAction.hashCode() : 0;
        result = 31 * result + (moveCardActions != null ? moveCardActions.hashCode() : 0);
        result = 31 * result + (drawFromBacklogActions != null ? drawFromBacklogActions.hashCode() : 0);
        result = 31 * result + (assignResourcesActions != null ? assignResourcesActions.hashCode() : 0);
        result = 31 * result + (diceCastActions != null ? diceCastActions.hashCode() : 0);
        return result;
    }
}

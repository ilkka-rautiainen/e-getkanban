package fi.aalto.ekanban.models;

public class Turn {

    private AdjustWipLimitsAction adjustWipLimitsAction;

    private DrawFromBacklogAction drawFromBacklogAction;

    public AdjustWipLimitsAction getAdjustWipLimitsAction() {
        return adjustWipLimitsAction;
    }

    public void setAdjustWipLimitsAction(AdjustWipLimitsAction adjustWipLimitsAction) {
        this.adjustWipLimitsAction = adjustWipLimitsAction;
    }

    public DrawFromBacklogAction getDrawFromBacklogAction() {
        return drawFromBacklogAction;
    }

    public void setDrawFromBacklogAction(DrawFromBacklogAction drawFromBacklogAction) {
        this.drawFromBacklogAction = drawFromBacklogAction;
    }
}

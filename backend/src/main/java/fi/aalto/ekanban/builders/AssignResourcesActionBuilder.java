package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.AssignResourcesAction;

public final class AssignResourcesActionBuilder {
    private String cardId;
    private String phaseId;
    private Integer points;

    private AssignResourcesActionBuilder() {
    }

    public static AssignResourcesActionBuilder anAssignResourcesAction() {
        return new AssignResourcesActionBuilder();
    }

    public AssignResourcesActionBuilder withCardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    public AssignResourcesActionBuilder withPhaseId(String phaseId) {
        this.phaseId = phaseId;
        return this;
    }

    public AssignResourcesActionBuilder withPoints(Integer points) {
        this.points = points;
        return this;
    }

    public AssignResourcesAction build() {
        AssignResourcesAction assignResourcesAction = new AssignResourcesAction();
        assignResourcesAction.setCardId(cardId);
        assignResourcesAction.setPhaseId(phaseId);
        assignResourcesAction.setPoints(points);
        return assignResourcesAction;
    }
}

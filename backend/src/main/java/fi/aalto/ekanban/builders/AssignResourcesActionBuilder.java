package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.AssignResourcesAction;

public final class AssignResourcesActionBuilder {
    private String cardId;
    private String cardPhaseId;
    private Integer dieIndex;
    private String diePhaseId;
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

    public AssignResourcesActionBuilder withCardPhaseId(String cardPhaseId) {
        this.cardPhaseId = cardPhaseId;
        return this;
    }

    public AssignResourcesActionBuilder withDieIndex(Integer dieIndex) {
        this.dieIndex = dieIndex;
        return this;
    }

    public AssignResourcesActionBuilder withDiePhaseId(String diePhaseId) {
        this.diePhaseId = diePhaseId;
        return this;
    }

    public AssignResourcesActionBuilder withPoints(Integer points) {
        this.points = points;
        return this;
    }

    public AssignResourcesAction build() {
        AssignResourcesAction assignResourcesAction = new AssignResourcesAction();
        assignResourcesAction.setCardId(cardId);
        assignResourcesAction.setCardPhaseId(cardPhaseId);
        assignResourcesAction.setDieIndex(dieIndex);
        assignResourcesAction.setDiePhaseId(diePhaseId);
        assignResourcesAction.setPoints(points);
        return assignResourcesAction;
    }
}

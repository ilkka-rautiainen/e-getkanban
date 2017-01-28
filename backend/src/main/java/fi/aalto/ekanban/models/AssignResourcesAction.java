package fi.aalto.ekanban.models;

public class AssignResourcesAction {
    private String cardId;
    private String phaseId;
    private Integer points;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(String phaseId) {
        this.phaseId = phaseId;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof AssignResourcesAction)) return false;

        AssignResourcesAction action = (AssignResourcesAction) o;

        if (cardId != null ? !cardId.equals(action.cardId) : action.cardId != null) return false;
        if (phaseId != null ? !phaseId.equals(action.phaseId) : action.phaseId != null) return false;
        return points != null ? points.equals(action.points) : action.points == null;

    }

    @Override
    public int hashCode() {
        int result = cardId != null ? cardId.hashCode() : 0;
        result = 31 * result + (phaseId != null ? phaseId.hashCode() : 0);
        result = 31 * result + (points != null ? points.hashCode() : 0);
        return result;
    }
}

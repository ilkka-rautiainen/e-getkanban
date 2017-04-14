package fi.aalto.ekanban.models;

public class AssignResourcesAction {
    private String cardId;
    private String cardPhaseId;
    private Integer dieIndex;
    private String diePhaseId;
    private Integer points;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCardPhaseId() {
        return cardPhaseId;
    }

    public void setCardPhaseId(String cardPhaseId) {
        this.cardPhaseId = cardPhaseId;
    }

    public Integer getDieIndex() {
        return dieIndex;
    }

    public void setDieIndex(Integer dieIndex) {
        this.dieIndex = dieIndex;
    }

    public String getDiePhaseId() {
        return diePhaseId;
    }

    public void setDiePhaseId(String diePhaseId) {
        this.diePhaseId = diePhaseId;
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
        if (cardPhaseId != null ? !cardPhaseId.equals(action.cardPhaseId) : action.cardPhaseId != null) return false;
        if (dieIndex != null ? !dieIndex.equals(action.dieIndex) : action.dieIndex != null) return false;
        if (diePhaseId!= null ? !diePhaseId.equals(action.diePhaseId) : action.diePhaseId!= null) return false;
        return points != null ? points.equals(action.points) : action.points == null;

    }

    @Override
    public int hashCode() {
        int result = cardId != null ? cardId.hashCode() : 0;
        result = 31 * result + (cardPhaseId != null ? cardPhaseId.hashCode() : 0);
        result = 31 * result + (diePhaseId != null ? diePhaseId.hashCode() : 0);
        result = 31 * result + (dieIndex != null ? dieIndex.hashCode() : 0);
        result = 31 * result + (points != null ? points.hashCode() : 0);
        return result;
    }
}

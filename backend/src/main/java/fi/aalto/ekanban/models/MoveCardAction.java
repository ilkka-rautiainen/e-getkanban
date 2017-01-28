package fi.aalto.ekanban.models;

public class MoveCardAction {

    private String fromColumnId;
    private String toColumnId;
    private String cardId;

    public String getFromColumnId() {
        return fromColumnId;
    }

    public void setFromColumnId(String fromColumnId) {
        this.fromColumnId = fromColumnId;
    }

    public String getToColumnId() {
        return toColumnId;
    }

    public void setToColumnId(String toColumnId) {
        this.toColumnId = toColumnId;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof MoveCardAction)) return false;

        MoveCardAction action = (MoveCardAction) o;

        if (fromColumnId != null ? !fromColumnId.equals(action.fromColumnId) : action.fromColumnId != null) return false;
        if (toColumnId != null ? !toColumnId.equals(action.toColumnId) : action.toColumnId != null) return false;
        return cardId != null ? cardId.equals(action.cardId) : action.cardId == null;

    }

    @Override
    public int hashCode() {
        int result = fromColumnId != null ? fromColumnId.hashCode() : 0;
        result = 31 * result + (toColumnId != null ? toColumnId.hashCode() : 0);
        result = 31 * result + (cardId != null ? cardId.hashCode() : 0);
        return result;
    }

}

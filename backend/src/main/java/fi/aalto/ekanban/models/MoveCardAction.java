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

}

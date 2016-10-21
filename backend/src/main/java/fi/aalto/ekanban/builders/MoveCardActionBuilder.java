package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.MoveCardAction;

public final class MoveCardActionBuilder {
    private String fromColumnId;
    private String toColumnId;
    private String cardId;

    private MoveCardActionBuilder() {
    }

    public static MoveCardActionBuilder aMoveCardAction() {
        return new MoveCardActionBuilder();
    }

    public MoveCardActionBuilder withFromColumnId(String fromColumnId) {
        this.fromColumnId = fromColumnId;
        return this;
    }

    public MoveCardActionBuilder withToColumnId(String toColumnId) {
        this.toColumnId = toColumnId;
        return this;
    }

    public MoveCardActionBuilder withCardId(String cardId) {
        this.cardId = cardId;
        return this;
    }

    public MoveCardAction build() {
        MoveCardAction moveCardAction = new MoveCardAction();
        moveCardAction.setFromColumnId(fromColumnId);
        moveCardAction.setToColumnId(toColumnId);
        moveCardAction.setCardId(cardId);
        return moveCardAction;
    }
}

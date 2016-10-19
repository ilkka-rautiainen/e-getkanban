package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.enums.BacklogDeckType;
import fi.aalto.ekanban.models.DrawFromBacklogAction;

public final class DrawFromBacklogActionBuilder {
    private BacklogDeckType deckType;
    private Integer indexToPlaceCardAt;

    private DrawFromBacklogActionBuilder() {
    }

    public static DrawFromBacklogActionBuilder aDrawFromBacklogAction() {
        return new DrawFromBacklogActionBuilder();
    }

    public DrawFromBacklogActionBuilder withDeckType(BacklogDeckType deckType) {
        this.deckType = deckType;
        return this;
    }

    public DrawFromBacklogActionBuilder withIndexToPlaceCardAt(Integer indexToPlaceCardAt) {
        this.indexToPlaceCardAt = indexToPlaceCardAt;
        return this;
    }

    public DrawFromBacklogAction build() {
        DrawFromBacklogAction drawFromBacklogAction = new DrawFromBacklogAction();
        drawFromBacklogAction.setDeckType(deckType);
        drawFromBacklogAction.setIndexToPlaceCardAt(indexToPlaceCardAt);
        return drawFromBacklogAction;
    }
}

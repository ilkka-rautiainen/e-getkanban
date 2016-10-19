package fi.aalto.ekanban.models;

import fi.aalto.ekanban.enums.BacklogDeckType;

public class DrawFromBacklogAction {

    private BacklogDeckType deckType;
    private Integer indexToPlaceCardAt;

    public BacklogDeckType getDeckType() {
        return deckType;
    }

    public void setDeckType(BacklogDeckType deckType) {
        this.deckType = deckType;
    }

    public Integer getIndexToPlaceCardAt() {
        return indexToPlaceCardAt;
    }

    public void setIndexToPlaceCardAt(Integer indexToPlaceCardAt) {
        this.indexToPlaceCardAt = indexToPlaceCardAt;
    }

}

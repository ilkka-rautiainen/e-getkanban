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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof DrawFromBacklogAction)) return false;

        DrawFromBacklogAction action = (DrawFromBacklogAction) o;

        if (deckType != null ? !deckType.equals(action.deckType) : action.deckType != null) return false;
        return indexToPlaceCardAt != null ? indexToPlaceCardAt.equals(action.indexToPlaceCardAt) : action.indexToPlaceCardAt == null;

    }

    @Override
    public int hashCode() {
        int result = deckType != null ? deckType.hashCode() : 0;
        result = 31 * result + (indexToPlaceCardAt != null ? indexToPlaceCardAt.hashCode() : 0);
        return result;
    }

}

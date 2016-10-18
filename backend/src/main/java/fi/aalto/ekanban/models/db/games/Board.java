package fi.aalto.ekanban.models.db.games;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.models.db.phases.Phase;

@Document
public class Board {

    @Id
    private String id;

    @Field
    private List<Card> backlogDeck;

    @Field
    private List<EventCard> eventCardDeck;

    @Field
    private List<Phase> phases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Card> getBacklogDeck() {
        return backlogDeck;
    }

    public void setBacklogDeck(List<Card> backlogDeck) {
        this.backlogDeck = backlogDeck;
    }

    public List<EventCard> getEventCardDeck() {
        return eventCardDeck;
    }

    public void setEventCardDeck(List<EventCard> eventCardDeck) {
        this.eventCardDeck = eventCardDeck;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        if (id != null ? !id.equals(board.id) : board.id != null) return false;
        if (backlogDeck != null ? !backlogDeck.equals(board.backlogDeck) : board.backlogDeck != null) return false;
        if (eventCardDeck != null ? !eventCardDeck.equals(board.eventCardDeck) : board.eventCardDeck != null)
            return false;
        return phases != null ? phases.equals(board.phases) : board.phases == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (backlogDeck != null ? backlogDeck.hashCode() : 0);
        result = 31 * result + (eventCardDeck != null ? eventCardDeck.hashCode() : 0);
        result = 31 * result + (phases != null ? phases.hashCode() : 0);
        return result;
    }

}

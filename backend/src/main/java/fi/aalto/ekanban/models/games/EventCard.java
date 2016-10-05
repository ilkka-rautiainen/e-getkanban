package fi.aalto.ekanban.models.games;

import java.util.List;

import fi.aalto.ekanban.models.GameOptionChange;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class EventCard {

    @Id
    private String id;

    @Field
    private String description;

    @Field
    private List<GameOptionChange> gameOptionChanges;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<GameOptionChange> getGameOptionChanges() {
        return gameOptionChanges;
    }

    public void setGameOptionChanges(List<GameOptionChange> gameOptionChanges) {
        this.gameOptionChanges = gameOptionChanges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EventCard)) return false;

        EventCard eventCard = (EventCard) o;

        if (id != null ? !id.equals(eventCard.id) : eventCard.id != null) return false;
        if (description != null ? !description.equals(eventCard.description) : eventCard.description != null)
            return false;
        return gameOptionChanges != null ? gameOptionChanges.equals(eventCard.gameOptionChanges) : eventCard.gameOptionChanges == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (gameOptionChanges != null ? gameOptionChanges.hashCode() : 0);
        return result;
    }
}

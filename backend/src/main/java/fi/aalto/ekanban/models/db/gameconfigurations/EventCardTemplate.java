package fi.aalto.ekanban.models.db.gameconfigurations;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/*
    Template for an event card. E.g. "Add {0} € money". To be instantiated to a game by an EventCardInstantiator.
 */
@Document
public class EventCardTemplate {

    @Id
    private String id;

    @Field
    private String name;

    @Field
    private String description;

    @Field
    private List<Integer> gameOptionChangeIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getGameOptionChangeIds() {
        return gameOptionChangeIds;
    }

    public void setGameOptionChangeIds(List<Integer> gameOptionChangeIds) {
        this.gameOptionChangeIds = gameOptionChangeIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof EventCardTemplate)) return false;

        EventCardTemplate that = (EventCardTemplate) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return gameOptionChangeIds != null ? gameOptionChangeIds.equals(that.gameOptionChangeIds) : that.gameOptionChangeIds == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (gameOptionChangeIds != null ? gameOptionChangeIds.hashCode() : 0);
        return result;
    }
}

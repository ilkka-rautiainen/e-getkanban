package fi.aalto.ekanban.models.db.gameconfigurations;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/*
    Contains parameters to instantiate an EventCardTemplate for a game
 */
@Document
public class EventCardInstantiator {

    @Id
    private String id;

    @Field
    private List<GameOptionChange> gameOptionChanges;

    @Field
    private String eventCardTemplateId;

    @Field
    private List<String> descriptionReplacements;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<GameOptionChange> getGameOptionChanges() {
        return gameOptionChanges;
    }

    public void setGameOptionChanges(List<GameOptionChange> gameOptionChanges) {
        this.gameOptionChanges = gameOptionChanges;
    }

    public String getEventCardTemplateId() {
        return eventCardTemplateId;
    }

    public void setEventCardTemplateId(String eventCardTemplateId) {
        this.eventCardTemplateId = eventCardTemplateId;
    }

    public List<String> getDescriptionReplacements() {
        return descriptionReplacements;
    }

    public void setDescriptionReplacements(List<String> descriptionReplacements) {
        this.descriptionReplacements = descriptionReplacements;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof EventCardInstantiator)) return false;

        EventCardInstantiator that = (EventCardInstantiator) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (gameOptionChanges != null ? !gameOptionChanges.equals(that.gameOptionChanges) : that.gameOptionChanges != null)
            return false;
        if (eventCardTemplateId != null ? !eventCardTemplateId.equals(that.eventCardTemplateId) : that.eventCardTemplateId != null)
            return false;
        return descriptionReplacements != null ? descriptionReplacements.equals(that.descriptionReplacements) : that.descriptionReplacements == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (gameOptionChanges != null ? gameOptionChanges.hashCode() : 0);
        result = 31 * result + (eventCardTemplateId != null ? eventCardTemplateId.hashCode() : 0);
        result = 31 * result + (descriptionReplacements != null ? descriptionReplacements.hashCode() : 0);
        return result;
    }

}

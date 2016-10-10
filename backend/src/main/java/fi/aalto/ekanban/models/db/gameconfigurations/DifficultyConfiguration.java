package fi.aalto.ekanban.models.db.gameconfigurations;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class DifficultyConfiguration {

    @Id
    private String id;

    @Field
    private String name;

    @Field
    private List<GameOptionChange> initialGameOptionChanges;

    @Field
    private List<EventCardInstantiator> eventCardConfigurations;

    @Field
    private List<String> baseCardIds;

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

    public List<GameOptionChange> getInitialGameOptionChanges() {
        return initialGameOptionChanges;
    }

    public void setInitialGameOptionChanges(List<GameOptionChange> initialGameOptionChanges) {
        this.initialGameOptionChanges = initialGameOptionChanges;
    }

    public List<EventCardInstantiator> getEventCardConfigurations() {
        return eventCardConfigurations;
    }

    public void setEventCardConfigurations(List<EventCardInstantiator> eventCardConfigurations) {
        this.eventCardConfigurations = eventCardConfigurations;
    }

    public List<String> getbaseCardIds() {
        return baseCardIds;
    }

    public void setbaseCardIds(List<String> baseCardIds) {
        this.baseCardIds = baseCardIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof DifficultyConfiguration)) return false;

        DifficultyConfiguration that = (DifficultyConfiguration) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (initialGameOptionChanges != null ? !initialGameOptionChanges.equals(that.initialGameOptionChanges) : that.initialGameOptionChanges != null)
            return false;
        if (eventCardConfigurations != null ? !eventCardConfigurations.equals(that.eventCardConfigurations) : that.eventCardConfigurations != null)
            return false;
        return baseCardIds != null ? baseCardIds.equals(that.baseCardIds) : that.baseCardIds == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (initialGameOptionChanges != null ? initialGameOptionChanges.hashCode() : 0);
        result = 31 * result + (eventCardConfigurations != null ? eventCardConfigurations.hashCode() : 0);
        result = 31 * result + (baseCardIds != null ? baseCardIds.hashCode() : 0);
        return result;
    }
}

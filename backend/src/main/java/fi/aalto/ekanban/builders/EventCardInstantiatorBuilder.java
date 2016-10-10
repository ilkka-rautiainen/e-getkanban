package fi.aalto.ekanban.builders;

import java.util.List;

import fi.aalto.ekanban.models.db.gameconfigurations.EventCardInstantiator;
import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;

public final class EventCardInstantiatorBuilder {
    private String id;
    private List<GameOptionChange> gameOptionChanges;
    private String eventCardTemplateId;
    private List<String> descriptionReplacements;

    private EventCardInstantiatorBuilder() {
    }

    public static EventCardInstantiatorBuilder aEventCardInstantiator() {
        return new EventCardInstantiatorBuilder();
    }

    public EventCardInstantiatorBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public EventCardInstantiatorBuilder withGameOptionChanges(List<GameOptionChange> gameOptionChanges) {
        this.gameOptionChanges = gameOptionChanges;
        return this;
    }

    public EventCardInstantiatorBuilder withEventCardTemplateId(String eventCardTemplateId) {
        this.eventCardTemplateId = eventCardTemplateId;
        return this;
    }

    public EventCardInstantiatorBuilder withDescriptionReplacements(List<String> descriptionReplacements) {
        this.descriptionReplacements = descriptionReplacements;
        return this;
    }

    public EventCardInstantiator build() {
        EventCardInstantiator EventCardInstantiator = new EventCardInstantiator();
        EventCardInstantiator.setId(id);
        EventCardInstantiator.setGameOptionChanges(gameOptionChanges);
        EventCardInstantiator.setEventCardTemplateId(eventCardTemplateId);
        EventCardInstantiator.setDescriptionReplacements(descriptionReplacements);
        return EventCardInstantiator;
    }
}

package fi.aalto.ekanban.builders;

import java.util.List;

import fi.aalto.ekanban.models.db.gameconfigurations.EventCardTemplate;

public final class EventCardTemplateBuilder {
    private String id;
    private String name;
    private String description;
    private List<Integer> gameOptionChangeIds;

    private EventCardTemplateBuilder() {
    }

    public static EventCardTemplateBuilder anEventCardTemplate() {
        return new EventCardTemplateBuilder();
    }

    public EventCardTemplateBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public EventCardTemplateBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public EventCardTemplateBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public EventCardTemplateBuilder withGameOptionChangeIds(List<Integer> gameOptionChangeIds) {
        this.gameOptionChangeIds = gameOptionChangeIds;
        return this;
    }

    public EventCardTemplate build() {
        EventCardTemplate eventCardTemplate = new EventCardTemplate();
        eventCardTemplate.setId(id);
        eventCardTemplate.setName(name);
        eventCardTemplate.setDescription(description);
        eventCardTemplate.setGameOptionChangeIds(gameOptionChangeIds);
        return eventCardTemplate;
    }
}

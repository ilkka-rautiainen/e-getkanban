package fi.aalto.ekanban.builders;

import java.util.List;

import fi.aalto.ekanban.models.BaseEventCardConfiguration;
import fi.aalto.ekanban.models.GameOptionChange;

public final class BaseEventCardConfigurationBuilder {
    private String id;
    private List<GameOptionChange> gameOptionChanges;
    private String eventCardTemplateId;
    private List<String> descriptionReplacements;

    private BaseEventCardConfigurationBuilder() {
    }

    public static BaseEventCardConfigurationBuilder aBaseEventCardConfiguration() {
        return new BaseEventCardConfigurationBuilder();
    }

    public BaseEventCardConfigurationBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public BaseEventCardConfigurationBuilder withGameOptionChanges(List<GameOptionChange> gameOptionChanges) {
        this.gameOptionChanges = gameOptionChanges;
        return this;
    }

    public BaseEventCardConfigurationBuilder withEventCardTemplateId(String eventCardTemplateId) {
        this.eventCardTemplateId = eventCardTemplateId;
        return this;
    }

    public BaseEventCardConfigurationBuilder withDescriptionReplacements(List<String> descriptionReplacements) {
        this.descriptionReplacements = descriptionReplacements;
        return this;
    }

    public BaseEventCardConfiguration build() {
        BaseEventCardConfiguration baseEventCardConfiguration = new BaseEventCardConfiguration();
        baseEventCardConfiguration.setId(id);
        baseEventCardConfiguration.setGameOptionChanges(gameOptionChanges);
        baseEventCardConfiguration.setEventCardTemplateId(eventCardTemplateId);
        baseEventCardConfiguration.setDescriptionReplacements(descriptionReplacements);
        return baseEventCardConfiguration;
    }
}

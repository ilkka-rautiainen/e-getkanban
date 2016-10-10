package fi.aalto.ekanban.builders;

import java.util.List;

import fi.aalto.ekanban.models.db.gameconfigurations.EventCardInstantiator;
import fi.aalto.ekanban.models.db.gameconfigurations.DifficultyConfiguration;
import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;

public final class DifficultyConfigurationBuilder {
    private String id;
    private String name;
    private List<GameOptionChange> initialGameOptionChanges;
    private List<EventCardInstantiator> eventCardConfigurations;

    private DifficultyConfigurationBuilder() {
    }

    public static DifficultyConfigurationBuilder aDifficultyConfiguration() {
        return new DifficultyConfigurationBuilder();
    }

    public DifficultyConfigurationBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public DifficultyConfigurationBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public DifficultyConfigurationBuilder withInitialGameOptionChanges(List<GameOptionChange> initialGameOptionChanges) {
        this.initialGameOptionChanges = initialGameOptionChanges;
        return this;
    }

    public DifficultyConfigurationBuilder withEventCardConfigurations(List<EventCardInstantiator> eventCardConfigurations) {
        this.eventCardConfigurations = eventCardConfigurations;
        return this;
    }

    public DifficultyConfiguration build() {
        DifficultyConfiguration difficultyConfiguration = new DifficultyConfiguration();
        difficultyConfiguration.setId(id);
        difficultyConfiguration.setName(name);
        difficultyConfiguration.setInitialGameOptionChanges(initialGameOptionChanges);
        difficultyConfiguration.setEventCardConfigurations(eventCardConfigurations);
        return difficultyConfiguration;
    }
}

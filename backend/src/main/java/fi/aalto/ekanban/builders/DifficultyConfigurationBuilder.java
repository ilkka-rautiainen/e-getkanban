package fi.aalto.ekanban.builders;

import java.util.ArrayList;
import java.util.List;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.gameconfigurations.DifficultyConfiguration;
import fi.aalto.ekanban.models.db.gameconfigurations.EventCardInstantiator;
import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;
import fi.aalto.ekanban.repositories.DifficultyConfigurationRepository;

public final class DifficultyConfigurationBuilder {
    private String id;
    private GameDifficulty gameDifficulty;
    private List<GameOptionChange> initialGameOptionChanges;
    private List<EventCardInstantiator> eventCardConfigurations;
    private List<String> backlogCardIds;
    private String boardEnteredTracklineColor;

    private DifficultyConfigurationBuilder() {
        initialGameOptionChanges = new ArrayList<>();
        eventCardConfigurations = new ArrayList<>();
        backlogCardIds = new ArrayList<>();
    }

    public static DifficultyConfigurationBuilder aDifficultyConfiguration() {
        return new DifficultyConfigurationBuilder();
    }

    public DifficultyConfigurationBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public DifficultyConfigurationBuilder withGameDifficulty(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
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

    public DifficultyConfigurationBuilder withBacklogCardIds(List<String> backlogCardIds) {
        this.backlogCardIds = backlogCardIds;
        return this;
    }

    public DifficultyConfigurationBuilder withBoardEnteredTracklineColor(String boardEnteredTracklineColor) {
        this.boardEnteredTracklineColor = boardEnteredTracklineColor;
        return this;
    }

    public DifficultyConfigurationBuilder withNormalDifficultyDefaults() {
        this.id = GameDifficulty.NORMAL.toString();
        this.gameDifficulty = GameDifficulty.NORMAL;
        this.boardEnteredTracklineColor = "551a8b";
        return this;
    }

    public DifficultyConfiguration createIfNotCreated(DifficultyConfigurationRepository repository) {
        DifficultyConfiguration diffConfig = repository.findOne(id);
        if (diffConfig != null) {
            return diffConfig;
        }
        else {
            diffConfig = build();
            return repository.save(diffConfig);
        }
    }

    public DifficultyConfiguration build() {
        DifficultyConfiguration difficultyConfiguration = new DifficultyConfiguration();
        difficultyConfiguration.setId(id);
        difficultyConfiguration.setGameDifficulty(gameDifficulty);
        difficultyConfiguration.setInitialGameOptionChanges(initialGameOptionChanges);
        difficultyConfiguration.setEventCardConfigurations(eventCardConfigurations);
        difficultyConfiguration.setBacklogCardIds(backlogCardIds);
        difficultyConfiguration.setBoardEnteredTracklineColor(boardEnteredTracklineColor);
        return difficultyConfiguration;
    }
}

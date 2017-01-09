package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.db.gameconfigurations.DifficultyConfiguration;
import fi.aalto.ekanban.repositories.DifficultyConfigurationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DifficultyConfigurationsBuilder {
    private List<DifficultyConfigurationBuilder> diffConfigBuilders;

    private DifficultyConfigurationsBuilder() {
        diffConfigBuilders = new ArrayList<>();
    }

    public static DifficultyConfigurationsBuilder aSetOfDifficultyConfigurations() {
        return new DifficultyConfigurationsBuilder();
    }

    public DifficultyConfigurationsBuilder withAllDifficultyConfigurations() {
        addNormalDiffConfig();
        return this;
    }

    private void addNormalDiffConfig() {
        diffConfigBuilders.add(DifficultyConfigurationBuilder.aDifficultyConfiguration()
                .withNormalDifficultyDefaults());
    }

    public List<DifficultyConfiguration> createIfNotCreated(DifficultyConfigurationRepository diffConfigRepository) {
        return diffConfigBuilders.stream()
                .map(diffConfigBuilder -> diffConfigBuilder.createIfNotCreated(diffConfigRepository))
                .collect(Collectors.toList());
    }
}

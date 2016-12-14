package fi.aalto.ekanban.builders;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.models.db.gameconfigurations.BaseCard;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.repositories.BaseCardRepository;

public class BaseCardsBuilder {
    private List<BaseCardBuilder> baseCardBuilders;

    public static BaseCardsBuilder aSetOfCards() {
        BaseCardsBuilder builder = new BaseCardsBuilder();
        builder.baseCardBuilders = new ArrayList<>();
        return builder;
    }

    public BaseCardsBuilder withAllBaseCardsWithMockPoints() {
        // TODO: implement with real points
        for (Integer i = 0; i < 5; i++) {
            baseCardBuilders.add(BaseCardBuilder.aBaseCard()
                    .withFinancialValue(FinancialValue.HIGH)
                    .withCardPhasePoints(getMockPoints())
                    .withSubscribesWhenDeployed("1"));
            baseCardBuilders.add(BaseCardBuilder.aBaseCard()
                    .withFinancialValue(FinancialValue.MED)
                    .withCardPhasePoints(getMockPoints())
                    .withSubscribesWhenDeployed("1"));
            baseCardBuilders.add(BaseCardBuilder.aBaseCard()
                    .withFinancialValue(FinancialValue.LOW)
                    .withCardPhasePoints(getMockPoints())
                    .withSubscribesWhenDeployed("1"));
        }
        return this;
    }

    public List<BaseCard> build() {
        return baseCardBuilders.stream()
                .map(BaseCardBuilder::build)
                .collect(Collectors.toList());
    }

    public List<BaseCard> createIfNotCreated(BaseCardRepository baseCardRepository) {
        if (baseCardRepository.count() > 0) {
            return baseCardRepository.findAll();
        }
        List<BaseCard> createdBaseCards = baseCardBuilders.stream()
                .map(baseCardBuilder -> baseCardBuilder.create(baseCardRepository))
                .collect(Collectors.toList());
        return createdBaseCards;
    }

    private List<CardPhasePoint> getMockPoints() {
        return CardPhasePointsBuilder.aSetOfCardPhasePoints()
                    .withNormalDifficultyMockPhasePoints()
                    .build();
    }

}

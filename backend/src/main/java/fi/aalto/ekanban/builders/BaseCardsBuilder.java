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

    public BaseCardsBuilder withBaseCardsForNormalDifficulty() {
        // 1-5
        addNormalDifficultyBaseCard(FinancialValue.LOW, 6, 9, 10);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 10, 12, 11);
        addNormalDifficultyBaseCard(FinancialValue.MED, 7, 8, 10);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 5, 7, 8);
        addNormalDifficultyBaseCard(FinancialValue.MED, 5, 6, 7);
        // 6-10
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 10, 9, 8);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 8, 12, 10);
        addNormalDifficultyBaseCard(FinancialValue.MED, 7, 8, 9);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 9, 6, 10);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 8, 5, 11);
        // 11-15
        addNormalDifficultyBaseCard(FinancialValue.LOW, 3, 4, 9);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 7, 8, 11);
        addNormalDifficultyBaseCard(FinancialValue.MED, 3, 5, 10);
        addNormalDifficultyBaseCard(FinancialValue.MED, 5, 6, 9);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 3, 5, 6);
        // 16-20
        addNormalDifficultyBaseCard(FinancialValue.LOW, 4, 5, 8);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 6, 8, 8);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 5, 6, 9);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 4, 5, 7);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 3, 5, 1);
        // 21-25
        addNormalDifficultyBaseCard(FinancialValue.LOW, 7, 8, 10);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 4, 4, 7);
        addNormalDifficultyBaseCard(FinancialValue.MED, 5, 7, 9);
        addNormalDifficultyBaseCard(FinancialValue.MED, 5, 6, 6);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 7, 8, 11);
        // 26-30
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 6, 7, 10);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 8, 8, 9);
        addNormalDifficultyBaseCard(FinancialValue.MED, 6, 7, 6);
        addNormalDifficultyBaseCard(FinancialValue.MED, 6, 6, 8);
        addNormalDifficultyBaseCard(FinancialValue.HIGH, 5, 4, 6);
        // 31-35
        addNormalDifficultyBaseCard(FinancialValue.MED, 6, 7, 7);
        addNormalDifficultyBaseCard(FinancialValue.MED, 5, 5, 5);
        addNormalDifficultyBaseCard(FinancialValue.MED, 4, 3, 4);
        addNormalDifficultyBaseCard(FinancialValue.LOW, 6, 7, 9);
        addNormalDifficultyBaseCard(FinancialValue.MED, 7, 6, 8);
        // 36
        addNormalDifficultyBaseCard(FinancialValue.MED, 5, 9, 10);

        return this;
    }

    private void addNormalDifficultyBaseCard(FinancialValue financialValue, Integer analysisPoints, Integer developmentPoints, Integer testPoints) {
        baseCardBuilders.add(BaseCardBuilder.aBaseCard()
                .withFinancialValue(financialValue)
                .withCardPhasePoints(CardPhasePointsBuilder.aSetOfCardPhasePoints()
                        .withAnalysisPoints(analysisPoints)
                        .withDevelopmentPoints(developmentPoints)
                        .withTestPoints(testPoints)
                        .build()));
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

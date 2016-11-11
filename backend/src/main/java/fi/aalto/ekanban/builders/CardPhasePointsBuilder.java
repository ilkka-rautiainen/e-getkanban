package fi.aalto.ekanban.builders;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.TEST_PHASE_ID;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import fi.aalto.ekanban.models.db.games.CardPhasePoint;

public class CardPhasePointsBuilder {

    private List<CardPhasePointBuilder> cardPhasePointBuilders;

    public static CardPhasePointsBuilder aSetOfCardPhasePoints() {
        CardPhasePointsBuilder builder = new CardPhasePointsBuilder();
        builder.cardPhasePointBuilders = new ArrayList<>();
        return builder;
    }

    public CardPhasePointsBuilder withNormalDifficultyMockPhasePoints() {
        CardPhasePointBuilder analysisCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                .withTotalPoints(5)
                .withPhaseId(ANALYSIS_PHASE_ID);
        cardPhasePointBuilders.add(analysisCardPoints);

        CardPhasePointBuilder developmentCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                .withTotalPoints(5)
                .withPhaseId(DEVELOPMENT_PHASE_ID);
        cardPhasePointBuilders.add(developmentCardPoints);

        CardPhasePointBuilder testCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                .withTotalPoints(5)
                .withPhaseId(TEST_PHASE_ID);
        cardPhasePointBuilders.add(testCardPoints);

        return this;
    }

    public List<CardPhasePoint> build() {
        return cardPhasePointBuilders.stream()
                .map(CardPhasePointBuilder::build)
                .collect(Collectors.toList());
    }

}

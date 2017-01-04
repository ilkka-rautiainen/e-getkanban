package fi.aalto.ekanban.builders;

import java.util.List;

import org.bson.types.ObjectId;

import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.models.db.gameconfigurations.BaseCard;
import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;

public final class CardBuilder {
    private String id;
    private List<CardPhasePoint> cardPhasePoints;
    private FinancialValue financialValue;
    private List<GameOptionChange> gameOptionChangesWhenDeployed;
    private String subscribesWhenDeployed;
    private String description;
    private String outcome;
    private Integer dayStarted;
    private Integer dayDeployed;
    private Integer leadTimeInDays;
    private Integer subscribers;
    private Integer orderNumber;

    private CardBuilder() {
        this.id = ObjectId.get().toString();
    }

    public static CardBuilder aCard() {
        return new CardBuilder();
    }

    public CardBuilder withDayStarted(Integer dayStarted) {
        this.dayStarted = dayStarted;
        return this;
    }

    public CardBuilder withDayDeployed(Integer dayDeployed) {
        this.dayDeployed = dayDeployed;
        return this;
    }

    public CardBuilder withSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
        return this;
    }

    public CardBuilder withLeadTimeInDays(Integer leadTimeInDays) {
        this.leadTimeInDays = leadTimeInDays;
        return this;
    }

    public CardBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CardBuilder withOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
        return this;
    }

    public CardBuilder withCardPhasePoints(List<CardPhasePoint> cardPhasePoints) {
        this.cardPhasePoints = cardPhasePoints;
        return this;
    }

    public CardBuilder withFinancialValue(FinancialValue financialValue) {
        this.financialValue = financialValue;
        return this;
    }

    public CardBuilder withGameOptionChangesWhenDeployed(List<GameOptionChange> gameOptionChangesWhenDeployed) {
        this.gameOptionChangesWhenDeployed = gameOptionChangesWhenDeployed;
        return this;
    }

    public CardBuilder withSubscribesWhenDeployed(String subscribesWhenDeployed) {
        this.subscribesWhenDeployed = subscribesWhenDeployed;
        return this;
    }

    public CardBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public CardBuilder withOutcome(String outcome) {
        this.outcome = outcome;
        return this;
    }

    public CardBuilder withMockPhasePoints() {
        this.cardPhasePoints = CardPhasePointsBuilder.aSetOfCardPhasePoints()
                .withNormalDifficultyMockPhasePoints()
                .build();
        return this;
    }

    public CardBuilder fromBaseCard(BaseCard baseCard) {
        this.id = baseCard.getId();
        this.cardPhasePoints = baseCard.getCardPhasePoints();
        this.financialValue = baseCard.getFinancialValue();
        this.gameOptionChangesWhenDeployed = baseCard.getGameOptionChangesWhenDeployed();
        this.subscribesWhenDeployed = baseCard.getSubscribesWhenDeployed();
        this.description = baseCard.getDescription();
        this.outcome = baseCard.getOutcome();
        return this;
    }

    public Card build() {
        Card card = new Card();
        card.setDayStarted(dayStarted);
        card.setDayDeployed(dayDeployed);
        card.setLeadTimeInDays(leadTimeInDays);
        card.setSubscribers(subscribers);
        card.setId(id);
        card.setOrderNumber(orderNumber);
        card.setCardPhasePoints(cardPhasePoints);
        card.setFinancialValue(financialValue);
        card.setGameOptionChangesWhenDeployed(gameOptionChangesWhenDeployed);
        card.setSubscribesWhenDeployed(subscribesWhenDeployed);
        card.setDescription(description);
        card.setOutcome(outcome);
        return card;
    }
}

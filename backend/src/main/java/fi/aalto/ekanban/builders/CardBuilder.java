package fi.aalto.ekanban.builders;

import java.util.List;

import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.models.games.Card;
import fi.aalto.ekanban.models.games.CardPhasePoint;
import fi.aalto.ekanban.models.GameOptionChange;

public final class CardBuilder {
    private String id;
    private List<CardPhasePoint> cardPhasePoints;
    private FinancialValue financialValue;
    private List<GameOptionChange> gameOptionChangesWhenDeployed;
    private String subscribesWhenDeployed;
    private Integer dayStarted;
    private Integer subscribers;
    private String description;
    private String outcome;

    private CardBuilder() {
    }

    public static CardBuilder aCard() {
        return new CardBuilder();
    }

    public CardBuilder withId(String id) {
        this.id = id;
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

    public CardBuilder withDayStarted(Integer dayStarted) {
        this.dayStarted = dayStarted;
        return this;
    }

    public CardBuilder withSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
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

    public Card build() {
        Card card = new Card();
        card.setId(id);
        card.setCardPhasePoints(cardPhasePoints);
        card.setFinancialValue(financialValue);
        card.setGameOptionChangesWhenDeployed(gameOptionChangesWhenDeployed);
        card.setSubscribesWhenDeployed(subscribesWhenDeployed);
        card.setDayStarted(dayStarted);
        card.setSubscribers(subscribers);
        card.setDescription(description);
        card.setOutcome(outcome);
        return card;
    }

}

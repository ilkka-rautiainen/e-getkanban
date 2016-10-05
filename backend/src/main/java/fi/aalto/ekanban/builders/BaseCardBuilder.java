package fi.aalto.ekanban.builders;

import java.util.List;

import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.models.BaseCard;
import fi.aalto.ekanban.models.games.CardPhasePoint;
import fi.aalto.ekanban.models.GameOptionChange;

public final class BaseCardBuilder {
    String id;
    List<CardPhasePoint> cardPhasePoints;
    FinancialValue financialValue;
    List<GameOptionChange> gameOptionChangesWhenDeployed;
    String subscribesWhenDeployed;
    String description;
    String outcome;

    private BaseCardBuilder() {
    }

    public static BaseCardBuilder aBaseCard() {
        return new BaseCardBuilder();
    }

    public BaseCardBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public BaseCardBuilder withCardPhasePoints(List<CardPhasePoint> cardPhasePoints) {
        this.cardPhasePoints = cardPhasePoints;
        return this;
    }

    public BaseCardBuilder withFinancialValue(FinancialValue financialValue) {
        this.financialValue = financialValue;
        return this;
    }

    public BaseCardBuilder withGameOptionChangesWhenDeployed(List<GameOptionChange> gameOptionChangesWhenDeployed) {
        this.gameOptionChangesWhenDeployed = gameOptionChangesWhenDeployed;
        return this;
    }

    public BaseCardBuilder withSubscribesWhenDeployed(String subscribesWhenDeployed) {
        this.subscribesWhenDeployed = subscribesWhenDeployed;
        return this;
    }

    public BaseCardBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public BaseCardBuilder withOutcome(String outcome) {
        this.outcome = outcome;
        return this;
    }

    public BaseCard build() {
        BaseCard baseCard = new BaseCard();
        baseCard.setId(id);
        baseCard.setCardPhasePoints(cardPhasePoints);
        baseCard.setFinancialValue(financialValue);
        baseCard.setGameOptionChangesWhenDeployed(gameOptionChangesWhenDeployed);
        baseCard.setSubscribesWhenDeployed(subscribesWhenDeployed);
        baseCard.setDescription(description);
        baseCard.setOutcome(outcome);
        return baseCard;
    }
}

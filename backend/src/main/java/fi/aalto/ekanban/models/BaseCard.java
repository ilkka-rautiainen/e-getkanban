package fi.aalto.ekanban.models;

import java.util.List;

import fi.aalto.ekanban.models.games.CardPhasePoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.enums.FinancialValue;

@Document
public class BaseCard {

    @Id
    protected String id;

    @Field
    protected List<CardPhasePoint> cardPhasePoints;

    @Field
    protected FinancialValue financialValue;

    @Field
    protected List<GameOptionChange> gameOptionChangesWhenDeployed;

    @Field
    protected String subscribesWhenDeployed;

    @Field
    protected String description;

    @Field
    protected String outcome;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CardPhasePoint> getCardPhasePoints() {
        return cardPhasePoints;
    }

    public void setCardPhasePoints(List<CardPhasePoint> cardPhasePoints) {
        this.cardPhasePoints = cardPhasePoints;
    }

    public FinancialValue getFinancialValue() {
        return financialValue;
    }

    public void setFinancialValue(FinancialValue financialValue) {
        this.financialValue = financialValue;
    }

    public List<GameOptionChange> getGameOptionChangesWhenDeployed() {
        return gameOptionChangesWhenDeployed;
    }

    public void setGameOptionChangesWhenDeployed(List<GameOptionChange> gameOptionChangesWhenDeployed) {
        this.gameOptionChangesWhenDeployed = gameOptionChangesWhenDeployed;
    }

    public String getSubscribesWhenDeployed() {
        return subscribesWhenDeployed;
    }

    public void setSubscribesWhenDeployed(String subscribesWhenDeployed) {
        this.subscribesWhenDeployed = subscribesWhenDeployed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof BaseCard)) return false;

        BaseCard baseCard = (BaseCard) o;

        if (id != null ? !id.equals(baseCard.id) : baseCard.id != null) return false;
        if (cardPhasePoints != null ? !cardPhasePoints.equals(baseCard.cardPhasePoints) : baseCard.cardPhasePoints != null)
            return false;
        if (financialValue != baseCard.financialValue) return false;
        if (gameOptionChangesWhenDeployed != null ? !gameOptionChangesWhenDeployed.equals(baseCard.gameOptionChangesWhenDeployed) : baseCard.gameOptionChangesWhenDeployed != null)
            return false;
        if (subscribesWhenDeployed != null ? !subscribesWhenDeployed.equals(baseCard.subscribesWhenDeployed) : baseCard.subscribesWhenDeployed != null)
            return false;
        if (description != null ? !description.equals(baseCard.description) : baseCard.description != null)
            return false;
        return outcome != null ? outcome.equals(baseCard.outcome) : baseCard.outcome == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (cardPhasePoints != null ? cardPhasePoints.hashCode() : 0);
        result = 31 * result + (financialValue != null ? financialValue.hashCode() : 0);
        result = 31 * result + (gameOptionChangesWhenDeployed != null ? gameOptionChangesWhenDeployed.hashCode() : 0);
        result = 31 * result + (subscribesWhenDeployed != null ? subscribesWhenDeployed.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (outcome != null ? outcome.hashCode() : 0);
        return result;
    }

}

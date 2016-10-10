package fi.aalto.ekanban.models.db.games;

import fi.aalto.ekanban.models.db.gameconfigurations.BaseCard;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Card extends BaseCard {

    @Field
    private Integer dayStarted;

    @Field
    private Integer subscribers;

    public Integer getDayStarted() {
        return dayStarted;
    }

    public void setDayStarted(Integer dayStarted) {
        this.dayStarted = dayStarted;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (id != null ? !id.equals(card.id) : card.id != null) return false;
        if (cardPhasePoints != null ? !cardPhasePoints.equals(card.cardPhasePoints) : card.cardPhasePoints != null)
            return false;
        if (financialValue != card.financialValue) return false;
        if (gameOptionChangesWhenDeployed != null ? !gameOptionChangesWhenDeployed.equals(card.gameOptionChangesWhenDeployed) : card.gameOptionChangesWhenDeployed != null)
            return false;
        if (subscribesWhenDeployed != null ? !subscribesWhenDeployed.equals(card.subscribesWhenDeployed) : card.subscribesWhenDeployed != null)
            return false;
        if (description != null ? !description.equals(card.description) : card.description != null)
            return false;
        if (outcome != null ? !outcome.equals(card.outcome) : card.outcome != null) return false;
        if (dayStarted != null ? !dayStarted.equals(card.dayStarted) : card.dayStarted != null) return false;
        return subscribers != null ? subscribers.equals(card.subscribers) : card.subscribers == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (dayStarted != null ? dayStarted.hashCode() : 0);
        result = 31 * result + (subscribers != null ? subscribers.hashCode() : 0);
        return result;
    }
}

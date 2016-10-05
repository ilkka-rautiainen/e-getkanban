package fi.aalto.ekanban.builders;

import org.bson.types.ObjectId;

import fi.aalto.ekanban.models.games.CardPhasePoint;

public final class CardPhasePointBuilder {
    private String id;
    private String phaseId;
    private Integer totalPoints;
    private Integer pointsDone;

    private CardPhasePointBuilder() {
        this.id = ObjectId.get().toString();
    }

    public static CardPhasePointBuilder aCardPhasePoint() {
        return new CardPhasePointBuilder();
    }

    public CardPhasePointBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public CardPhasePointBuilder withPhaseId(String phaseId) {
        this.phaseId = phaseId;
        return this;
    }

    public CardPhasePointBuilder withTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public CardPhasePointBuilder withPointsDone(Integer pointsDone) {
        this.pointsDone = pointsDone;
        return this;
    }

    public CardPhasePoint build() {
        CardPhasePoint cardPhasePoint = new CardPhasePoint();
        cardPhasePoint.setId(id);
        cardPhasePoint.setPhaseId(phaseId);
        cardPhasePoint.setTotalPoints(totalPoints);
        cardPhasePoint.setPointsDone(pointsDone);
        return cardPhasePoint;
    }
}

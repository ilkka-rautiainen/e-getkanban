package fi.aalto.ekanban.builders;

import java.util.List;

import org.bson.types.ObjectId;

import fi.aalto.ekanban.models.db.games.EventCard;
import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;

public final class EventCardBuilder {
    private String id;
    private String description;
    private List<GameOptionChange> gameOptionChanges;

    private EventCardBuilder() {
        this.id = ObjectId.get().toString();
    }

    public static EventCardBuilder anEventCard() {
        return new EventCardBuilder();
    }

    public EventCardBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public EventCardBuilder withDescription(String description) {
        this.description = description;
        return this;
    }

    public EventCardBuilder withGameOptionChanges(List<GameOptionChange> gameOptionChanges) {
        this.gameOptionChanges = gameOptionChanges;
        return this;
    }

    public EventCard build() {
        EventCard eventCard = new EventCard();
        eventCard.setId(id);
        eventCard.setDescription(description);
        eventCard.setGameOptionChanges(gameOptionChanges);
        return eventCard;
    }
}

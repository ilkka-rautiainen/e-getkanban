package fi.aalto.ekanban.builders;

import java.util.List;

import org.bson.types.ObjectId;

import fi.aalto.ekanban.models.db.games.Board;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.EventCard;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

public final class BoardBuilder {
    private String id;
    private List<Card> backlogDeck;
    private List<EventCard> eventCardDeck;
    private List<Phase> phases;

    private BoardBuilder() {
        this.id = ObjectId.get().toString();
    }

    public static BoardBuilder aBoard() {
        return new BoardBuilder();
    }

    public BoardBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public BoardBuilder withBacklogDeck(List<Card> backlogDeck) {
        this.backlogDeck = backlogDeck;
        return this;
    }

    public BoardBuilder withEventCardDeck(List<EventCard> eventCardDeck) {
        this.eventCardDeck = eventCardDeck;
        return this;
    }

    public BoardBuilder withPhases(List<Phase> phases) {
        this.phases = phases;
        return this;
    }

    public BoardBuilder withNormalDifficultyDefaults(BaseCardRepository baseCardRepository,
                                                     PhaseRepository phaseRepository) {
        this.backlogDeck = CardsBuilder.aSetOfCards()
                .withNormalDifficultyBacklog(baseCardRepository)
                .build();
        this.phases = PhasesBuilder.normalDifficultyPhases(phaseRepository);
        return this;
    }

    public BoardBuilder withNormalDifficultyMockDefaults() {
        this.backlogDeck = CardsBuilder.aSetOfCards()
                .withNormalDifficultyMockBacklog()
                .build();
        this.phases = PhasesBuilder.aSetOfPhases()
                .withNormalDifficultyMockPhases()
                .build();
        return this;
    }

    public Board build() {
        Board board = new Board();
        board.setId(id);
        board.setBacklogDeck(backlogDeck);
        board.setEventCardDeck(eventCardDeck);
        board.setPhases(phases);
        return board;
    }
}

package fi.aalto.ekanban.builders;

import static fi.aalto.ekanban.ApplicationConstants.IN_PROGRESS_COLUMN;
import static fi.aalto.ekanban.ApplicationConstants.DONE_COLUMN;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.phases.Column;

public final class ColumnBuilder {
    private String id;
    private List<Card> cards;
    private String name;

    private ColumnBuilder() {
        this.id = ObjectId.get().toString();
    }

    public static ColumnBuilder aColumn() {
        return new ColumnBuilder();
    }

    public ColumnBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ColumnBuilder withCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

    public ColumnBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ColumnBuilder inProgress() {
        this.name = IN_PROGRESS_COLUMN;
        this.cards = new ArrayList<>();
        return this;
    }

    public ColumnBuilder done() {
        this.name = DONE_COLUMN;
        this.cards = new ArrayList<>();
        return this;
    }

    public Column build() {
        Column column = new Column();
        column.setId(id);
        column.setCards(cards);
        column.setName(name);
        return column;
    }
}

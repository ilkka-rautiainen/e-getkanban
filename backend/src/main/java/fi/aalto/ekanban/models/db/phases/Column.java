package fi.aalto.ekanban.models.db.phases;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.exceptions.CardNotFoundException;
import fi.aalto.ekanban.models.db.games.Card;

@Document
public class Column {

    @Id
    private String id;

    @Field
    private List<Card> cards;

    @Field
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Column)) return false;

        Column column = (Column) o;

        if (id != null ? !id.equals(column.id) : column.id != null) return false;
        if (cards != null ? !cards.equals(column.cards) : column.cards != null) return false;
        return name != null ? name.equals(column.name) : column.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (cards != null ? cards.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public Boolean isValid() {
        return cards != null;
    }

    public Boolean hasCard(String cardId) {
        return cards.stream().anyMatch(card -> card.getId().equals(cardId));
    }

    public Card pullCard(String cardId) throws CardNotFoundException {
        Optional<Card> card = cards.stream().filter(c -> c.getId().equals(cardId)).findFirst();
        if (!card.isPresent()) {
            throw new CardNotFoundException(MessageFormat.format(
                    "Column with id {0} can''t pull card with id {1} because it doesn''t exist",
                    getId(), cardId));
        }
        cards = cards.stream().filter(c -> !c.getId().equals(cardId)).collect(Collectors.toList());
        return card.get();
    }

    public void pushCard(Card card) {
        cards.add(0, card);
    }
}

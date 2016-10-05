package fi.aalto.ekanban.models;

import fi.aalto.ekanban.models.games.Card;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class CardTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Card.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

}

package fi.aalto.ekanban.models.db.games;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class EventCardTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(EventCard.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

}

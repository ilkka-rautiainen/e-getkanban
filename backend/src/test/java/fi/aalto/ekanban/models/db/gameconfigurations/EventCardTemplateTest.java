package fi.aalto.ekanban.models.db.gameconfigurations;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class EventCardTemplateTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(EventCardTemplate.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

}

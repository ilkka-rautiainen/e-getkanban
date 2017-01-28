package fi.aalto.ekanban.models.db.phases;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import fi.aalto.ekanban.models.db.phases.Phase;

public class PhaseTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Phase.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

}

package fi.aalto.ekanban.models.db.games;

import fi.aalto.ekanban.models.db.gameconfigurations.Phase;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class PhaseTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Phase.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

}

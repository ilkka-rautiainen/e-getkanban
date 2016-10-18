package fi.aalto.ekanban.models.db.games;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import fi.aalto.ekanban.models.db.phases.Column;

public class ColumnTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Column.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

}

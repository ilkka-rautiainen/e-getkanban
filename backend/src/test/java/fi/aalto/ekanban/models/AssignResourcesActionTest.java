package fi.aalto.ekanban.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

public class AssignResourcesActionTest {

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(AssignResourcesAction.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }

}

package fi.aalto.ekanban.models;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.Test;

import fi.aalto.ekanban.models.CFDDailyValue;

public class CFDDailyValueTest {
    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(CFDDailyValue.class).suppress(Warning.NONFINAL_FIELDS).usingGetClass().verify();
    }
}

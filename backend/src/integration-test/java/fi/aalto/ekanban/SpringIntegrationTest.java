package fi.aalto.ekanban;

import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringIntegrationTest {

    protected static final Logger logger = LoggerFactory.getLogger(SpringIntegrationTest.class);

    @Before
    public void setUpBaseClass() throws Exception {
        new TestContextManager(getClass()).prepareTestInstance(this);
    }

}

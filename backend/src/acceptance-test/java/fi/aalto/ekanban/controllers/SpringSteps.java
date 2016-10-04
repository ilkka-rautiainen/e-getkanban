package fi.aalto.ekanban.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringSteps {

    protected static final Logger logger =
            LoggerFactory.getLogger(SpringSteps.class);

    private TestContextManager testContextManager;

    public SpringSteps() {
        try {
            this.testContextManager = new TestContextManager(getClass());
            this.testContextManager.prepareTestInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

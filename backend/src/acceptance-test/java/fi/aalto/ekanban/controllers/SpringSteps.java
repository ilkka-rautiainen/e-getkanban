package fi.aalto.ekanban.controllers;

import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestContextManager;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class SpringSteps {

    protected static final Logger logger =
            LoggerFactory.getLogger(SpringSteps.class);

    @Value("${local.server.port}")
    int port;

    private TestContextManager testContextManager;

    public SpringSteps() {
        try {
            this.testContextManager = new TestContextManager(getClass());
            this.testContextManager.prepareTestInstance(this);
            RestAssured.port = port;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package fi.aalto.ekanban.controllers;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.is;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.repositories.GameRepository;

public class Stepdefs extends SpringSteps {

    @Value("${local.server.port}")
    int port;

    @Autowired
    private GameRepository gameRepository;

    private Response response;

    @Before
    public void setUp() {
        RestAssured.port = port;
        gameRepository.deleteAll();
    }

    @After
    public void reset() {
        gameRepository.deleteAll();
    }

    @Given("^I have first (\\d+) game\\(s\\)$")
    public void i_have_games(Integer count) throws Throwable {
        for (Integer i = 0; i < count; i++) {
            GameBuilder.aGame()
                    .withId(i.toString())
                    .withPlayerName("player " + i.toString())
                    .create(gameRepository);
        }
    }

    @When("^I call the action get games$")
    public void i_call_the_action_get_games() throws Throwable {
        response = when().get("/games");
    }

    @Then("^response status code should be (\\d+)$")
    public void response_status_code_should_be(Integer statusCode) throws Throwable {
        response.then().statusCode(statusCode);
    }

    @Then("^I should get (\\d+) game\\(s\\) in response$")
    public void i_should_get_games_in_response(Integer count) throws Throwable {
        logger.info(response.prettyPrint());
        response.then().body("size()", is(count));
    }

}

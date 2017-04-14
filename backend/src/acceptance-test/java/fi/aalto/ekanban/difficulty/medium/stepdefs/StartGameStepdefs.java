package fi.aalto.ekanban.difficulty.medium.stepdefs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;
import static fi.aalto.ekanban.ApplicationConstants.GAME_PATH;

import java.net.HttpURLConnection;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.SpringSteps;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.repositories.GameRepository;


public class StartGameStepdefs extends SpringSteps {

    @Autowired
    private GameRepository gameRepository;

    private ValidatableResponse response;
    private RequestSpecification request;

    @Before
    public void setUp() {
        gameRepository.deleteAll();
    }

    @After
    public void reset() {
        gameRepository.deleteAll();
    }

    @Given("^I enter player name as (.+)$")
    public void i_enter_player_name_as_(String playerName) throws Throwable {
        request = given().formParam("playerName", playerName);
    }

    @Given("^I choose (.+) difficulty$")
    public void i_choose_difficulty(String gameDifficulty) throws Throwable {
        gameDifficulty = gameDifficulty.toUpperCase();
        GameDifficulty chosenDifficulty = GameDifficulty.valueOf(gameDifficulty);
        request = request.formParam("difficultyLevel", chosenDifficulty);
    }

    @When("^I press start game$")
    public void i_press_start_game() throws Throwable {
        Response resp = request.when().post(GAME_PATH);
        response = resp.then();
    }

    @Then("^I should get a new game$")
    public void i_should_get_a_new_game() throws Throwable {
        response.statusCode(HttpURLConnection.HTTP_OK)
                .body(is(notNullValue()));
    }

    @And("^game should have player name as (.+)$")
    public void game_should_have_player_name_as(String playerName) throws Throwable {
        response.body("playerName", equalTo(playerName));
    }

    @And("^game should have current day of (\\d+)$")
    public void gameShouldHaveCurrentDayOf(int currentDay) throws Throwable {
        response.body("currentDay", equalTo(currentDay));
    }

    @And("^game should have difficulty of (.+)")
    public void gameShouldHaveDifficultyOf(String difficultyLevelName) throws Throwable {
        response.body("difficultyLevel", equalTo(difficultyLevelName.toUpperCase()));
    }

}

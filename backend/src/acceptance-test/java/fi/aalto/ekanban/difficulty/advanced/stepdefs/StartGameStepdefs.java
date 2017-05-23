package fi.aalto.ekanban.difficulty.advanced.stepdefs;

import static fi.aalto.ekanban.ApplicationConstants.GAME_PATH;
import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import java.net.HttpURLConnection;
import java.text.MessageFormat;

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

    @And("^game should have difficulty of (.+)")
    public void gameShouldHaveDifficultyOf(String difficultyLevelName) throws Throwable {
        response.body("difficultyLevel", equalTo(difficultyLevelName.toUpperCase()));
    }

    @And("^game should include a board$")
    public void game_should_include_a_board() throws Throwable {
        response.body("board", is(notNullValue()));
    }

    @And("^board should include different phases$")
    public void board_should_include_different_phases() throws Throwable {
        response.body("board.phases", is(notNullValue()));
    }

    @And("^(\\d+). phase in board is (.+)$")
    public void phase_in_board_is(Integer phaseNumber, String phaseName) throws Throwable {
        response.body(MessageFormat.format("board.phases.name[{0}]", phaseNumber-1), is(phaseName));
    }

    @And("^(.+) column of (.+) should allow new cards to be drawn in every (.*)$")
    public void column_of_should_allow_new_cards_to_be_drawn_in_every_day(String columnName, String phaseName, String day) {
        response.body("board.phases.find { it.id == '" + phaseName.toUpperCase() + "' }" +
                ".columns.find { it.name == '" + columnName+ "' }.dayMultiplierToEnter", equalTo(getDayAsInt(day)));
    }

    @And("^column of (.+) should allow new cards to be drawn in every (.*)")
    public void column_of_should_allow_new_cards_to_be_drawn_in_every_day(String phaseName, String day) {
        response.body("board.phases.find { it.id == '" + phaseName.toUpperCase() + "' }.columns[0].dayMultiplierToEnter",
                equalTo(getDayAsInt(day)));
    }

    private Integer getDayAsInt(String day) {
        return day.equals("third day") ? 3 : 1;
    }

}

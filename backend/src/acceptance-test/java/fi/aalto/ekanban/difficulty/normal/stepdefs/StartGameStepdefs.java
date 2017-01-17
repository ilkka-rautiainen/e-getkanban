package fi.aalto.ekanban.difficulty.normal.stepdefs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import static fi.aalto.ekanban.ApplicationConstants.GAME_PATH;

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

import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.SpringSteps;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

public class StartGameStepdefs extends SpringSteps {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PhaseRepository phaseRepository;

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

    @And("^game should include a board$")
    public void game_should_include_a_board() throws Throwable {
        response.body("board", is(notNullValue()));
    }

    @And("^board should include (\\d+) backlog deck$")
    public void board_should_include_backlog_deck(Integer deckCount) throws Throwable {
        response.body("board.backlogDeck", is(notNullValue()));
    }

    @And("^backlog deck should have (\\d+) cards$")
    public void backlog_deck_should_have_cards(Integer cardCount) throws Throwable {
        response.body("board.backlogDeck.size()", is(cardCount));
    }

    @And("^each card should contain phase points$")
    public void each_card_should_contain_phase_points() throws Throwable {
        //this checks every item in backlogDeck
        response.body("board.backlogDeck.cardPhasePoints", is(notNullValue()));
    }

    @And("^(\\d+). phase is (.+)$")
    public void phase_is(Integer phaseNumber, String phaseName) throws Throwable {
        Phase phaseWithGivenName = phaseRepository.findByName(phaseName);
        response.body("board.backlogDeck[0].cardPhasePoints[" + (phaseNumber-1) + "].phaseId",
                is(phaseWithGivenName.getId()));
    }

    @And("^each card should have empty day started$")
    public void each_card_should_have_empty_day_started() throws Throwable {
        response.body("board.backlogDeck.dayStarted[0]", is(nullValue()));
    }

    @And("^each card should have empty day deployed$")
    public void each_card_should_have_empty_day_deployed() throws Throwable {
        response.body("board.backlogDeck.dayDeployed[0]", is(nullValue()));
    }

    @And("^each card should have financial value$")
    public void each_card_should_have_financial_value() throws Throwable {
        response.body("board.backlogDeck[0].financialValue", is(notNullValue()));
        response.body("board.backlogDeck[0].financialValue", is(anyOf(
                equalTo(FinancialValue.LOW.toString()),
                equalTo(FinancialValue.MED.toString()),
                equalTo(FinancialValue.HIGH.toString()
        ))));
    }

    @And("^board should include event card deck$")
    public void board_should_include_event_card_deck() throws Throwable {
        response.body("board.eventCardDeck", is(notNullValue()));
    }

    @And("^event card deck should have (\\d+) event cards$")
    public void event_card_deck_should_have_event_cards(Integer cardCount) throws Throwable {
        response.body("board.eventCardDeck.size()", is(cardCount));
    }

    @And("^board should include different phases$")
    public void board_should_include_different_phases() throws Throwable {
        response.body("board.phases", is(notNullValue()));
    }

    @And("^(\\d+). phase in board is (.+)$")
    public void phase_in_board_is(Integer phaseNumber, String phaseName) throws Throwable {
        response.body(MessageFormat.format("board.phases.name[{0}]", phaseNumber-1), is(phaseName));
    }

    @And("^the phase (.+) should have (\\d+) column\\(s\\)$")
    public void analysis_should_have_two_columns(String phaseName, Integer columnCount) throws Throwable {
        response.body("board.phases.find { it.name == '" + phaseName + "'}.columns.size()", equalTo(columnCount));
    }

    @And("^(\\d+). column in phase (.+) is (.+)$")
    public void column_in_phase_is(Integer columnNumber, String phaseName, String columnName) throws Throwable {
        response.body("board.phases.find { it.name == '" + phaseName + "'}.columns[" + (columnNumber-1) + "].name",
                equalTo(columnName));
    }

    @And("^the game should include a CFD-diagram$")
    public void the_game_should_include_a_CFD_diagram() throws Throwable {
        response.body("cfd", is(notNullValue()));
    }

    @And("^the CFD-diagram should include a line for the cards passed the track line of phase (.+)")
    public void the_CFD_diagram_should_include_a_line_for_the_cards_passed_the_track_line_of_phase(String phaseName)
            throws Throwable {
        String phaseId = response.extract().path("board.phases.find { it.name == '" + phaseName + "' }.id");
        Integer newCurrentDay = response.extract().path("currentDay");
        String pathForDailyValueInPhase =
                "cfd.cfdDailyValues[" + Integer.toString(newCurrentDay) + "].phaseValues." + phaseId;
        response.body(pathForDailyValueInPhase, is(notNullValue()));
    }
}

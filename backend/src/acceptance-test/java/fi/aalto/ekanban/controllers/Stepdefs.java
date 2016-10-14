package fi.aalto.ekanban.controllers;

import static io.restassured.RestAssured.given;
import static fi.aalto.ekanban.ApplicationConstants.GAME_PATH;
import static org.hamcrest.Matchers.*;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.gameconfigurations.Phase;
import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

public class Stepdefs extends SpringSteps {

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
        logger.info(resp.prettyPrint());
        response = resp.then();
    }

    @Then("^I should get a new game$")
    public void i_should_get_a_new_game() throws Throwable {
        response.statusCode(200)
                .body(is(notNullValue()));
    }

    @Then("^game should have player name as (.+)$")
    public void game_should_have_player_name_as(String playerName) throws Throwable {
        response.body("playerName", equalTo(playerName));
    }

    @Then("^game should include a board$")
    public void game_should_include_a_board() throws Throwable {
        response.body("board", is(notNullValue()));
    }

    @Then("^board should include (\\d+) backlog deck$")
    public void board_should_include_backlog_deck(Integer deckCount) throws Throwable {
        response.body("board.backlogDeck", is(notNullValue()));
    }

    @Then("^backlog deck should have (\\d+) cards$")
    public void backlog_deck_should_have_cards(Integer cardCount) throws Throwable {
        response.body("board.backlogDeck.size()", is(cardCount));
    }

    @Then("^each card should contain phase points$")
    public void each_card_should_contain_phase_points() throws Throwable {
        //this checks every item in backlogDeck
        response.body("board.backlogDeck.cardPhasePoints", is(notNullValue()));
    }

    @Then("^(\\d+). phase is (.+)$")
    public void phase_is(Integer phaseNumber, String phaseName) throws Throwable {
        if (phaseName.equals("Dev"))
            phaseName = "Development";
        Phase phaseByGivenName = phaseRepository.findByName(phaseName);
        //This gets all the phaseIds to array and then checks by index
        response.body("board.backlogDeck.cardPhasePoints.phaseId[0]["+(phaseNumber-1)+"]",
                is(phaseByGivenName.getId()));
    }

    @Then("^each card should have empty day started$")
    public void each_card_should_have_empty_day_started() throws Throwable {
        response.body("board.backlogDeck.dayStarted[0]", is(nullValue()));
    }

    @Then("^each card should have empty day deployed$")
    public void each_card_should_have_empty_day_deployed() throws Throwable {
        response.body("board.backlogDeck.dayDeployed[0]", is(nullValue()));
    }

    @Then("^each card should have financial value$")
    public void each_card_should_have_financial_value() throws Throwable {
        response.body("board.backlogDeck.financialValue[0]", is(notNullValue()));
        response.body("board.backlogDeck.financialValue[0]", is(anyOf(
                equalTo(FinancialValue.LOW.toString()),
                equalTo(FinancialValue.MED.toString()),
                equalTo(FinancialValue.HIGH.toString()
        ))));
    }

    @Then("^board should include event card deck$")
    public void board_should_include_event_card_deck() throws Throwable {
        response.body("board.eventCardDeck", is(notNullValue()));
    }

    @Then("^event card deck should have (\\d+) event cards$")
    public void event_card_deck_should_have_event_cards(Integer cardCount) throws Throwable {
        response.body("board.eventCardDeck.size()", is(cardCount));
    }

    @Then("^board should include different phases$")
    public void board_should_include_different_phases() throws Throwable {
        response.body("board.phases", is(notNullValue()));
    }

    @Then("^(\\d+). phase in board is (.+)$")
    public void phase_in_board_is(Integer phaseNumber, String phaseName) throws Throwable {
        response.body("board.phases.name["+(phaseNumber-1)+"]", is(phaseName));
    }

    @Then("^(.+) should have (\\d+) column\\(s\\)$")
    public void analysis_should_have_two_columns(String phaseName, Integer columnCount) throws Throwable {
        response.body("board.phases.find { it.name == '"+phaseName+"'}.columns.size()", equalTo(columnCount));
    }

    @Then("^(\\d+). column of (.+) is (.+)$")
    public void column_is(Integer columnNumber, String phaseName, String columnName) throws Throwable {
        response.body("board.phases.find { it.name == '"+phaseName+"'}.columns.name["+(columnNumber-1)+"]",
                equalTo(columnName));
    }

}

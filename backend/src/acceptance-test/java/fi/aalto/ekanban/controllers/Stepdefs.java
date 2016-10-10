package fi.aalto.ekanban.controllers;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.repositories.GameRepository;

public class Stepdefs extends SpringSteps {

    @Autowired
    private GameRepository gameRepository;

    private Response response;

    @Before
    public void setUp() {
        gameRepository.deleteAll();
    }

    @After
    public void reset() {
        gameRepository.deleteAll();
    }

    @Given("^I choose (.+) difficulty$")
    public void i_choose_difficulty(String gameDifficulty) throws Throwable {
        // Write code here that turns the phrase above into concrete actions

    }

    @Given("^I enter player name as (.+)$")
    public void i_enter_player_name_as_(String playerName) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @When("^I press start game$")
    public void i_press_start_game() throws Throwable {
        // Write code here that turns the phrase above into concrete actions

    }

    @Then("^I should get a new game$")
    public void i_should_get_a_new_game() throws Throwable {
        // Write code here that turns the phrase above into concrete actions

    }

    @Then("^game should have player name as (.+)$")
    public void game_should_have_player_name_as(String playerName) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^game should include a board$")
    public void game_should_include_a_board() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^board should include (\\d+) backlog deck$")
    public void board_should_include_backlog_deck(Integer deckCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^backlog deck should have (\\d+) cards$")
    public void backlog_deck_should_have_cards(Integer cardCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^each card should contain phase points$")
    public void each_card_should_contain_phase_points() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^(\\d+). phase is (.+)$")
    public void st_phase_is_Analysis(Integer phaseNumber, String phaseName) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^each card should have empty day started$")
    public void each_card_should_have_empty_day_started() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^each card should have empty day deployed$")
    public void each_card_should_have_empty_day_deployed() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^each card should have financial value$")
    public void each_card_should_have_financial_value() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^board should include event card deck$")
    public void board_should_include_event_card_deck() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^event card deck should have (\\d+) event cards$")
    public void event_card_deck_should_have_event_cards(Integer cardCount) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^board should include different phases$")
    public void board_should_include_different_phases() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^(\\d+). is (.+)$")
    public void phase_is_(Integer phaseNumber, String phaseName) throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^Analysis should have two columns$")
    public void analysis_should_have_two_columns() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^first column is In Progress$")
    public void first_column_is_In_Progress() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^second column is Done$")
    public void second_column_is_Done() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^Development should have two columns$")
    public void development_should_have_two_columns() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^Test should have one column$")
    public void test_should_have_one_column() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

    @Then("^Deployed should have one column$")
    public void deployed_should_have_one_column() throws Throwable {
        // Write code here that turns the phrase above into concrete actions
    }

}

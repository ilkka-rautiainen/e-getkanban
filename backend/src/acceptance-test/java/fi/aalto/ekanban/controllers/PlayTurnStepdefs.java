package fi.aalto.ekanban.controllers;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import static fi.aalto.ekanban.ApplicationConstants.*;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.AdjustWipLimitsActionBuilder;
import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;
import fi.aalto.ekanban.utils.TestGameContainer;

public class PlayTurnStepdefs extends SpringSteps {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PhaseRepository phaseRepository;

    private TestGameContainer initialGameContainer;
    private RequestSpecification request;
    private AdjustWipLimitsAction adjustWipLimitsAction;

    private ValidatableResponse response;
    private Card cardInAnalysisBefore;
    private Card cardInDevelopmentBefore;
    private Card cardInTestBefore;

    @Before
    public void setUp() {
        gameRepository.deleteAll();
    }

    @After
    public void reset() {
        gameRepository.deleteAll();
    }

    @Given("^I have a game with difficulty of Normal$")
    public void i_have_a_game_with_difficulty_of_normal()
            throws Throwable {
        initGame();
        initRequest();
    }

    private void initGame() {
        initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
        gameRepository.save(initialGameContainer.getGame());
    }

    private void initRequest() {
        request = given()
                .pathParam("gameId", initialGameContainer.getGame().getId());
        adjustWipLimitsAction = AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction()
                .withPhaseWipLimits(new HashMap<>())
                .build();
    }

    @And("^game has one almost done card in first columns of the work phases$")
    public void game_has_one_almost_done_card_in_first_columns_of_the_work_phases() throws Throwable {
        initialGameContainer.fillFirstWorkPhasesWithSomeAlmostReadyCards();
        initialGameContainer.fillLastWorkPhaseWithSomeAlmostReadyCards();
        gameRepository.save(initialGameContainer.getGame());
        initCards();
    }

    @And("^game has one ready card in first columns of the work phases$")
    public void game_has_one_ready_card_in_first_columns_of_the_work_phases() throws Throwable {
        initialGameContainer.fillFirstWorkPhasesWithSomeReadyCards();
        initialGameContainer.fillLastWorkPhaseWithSomeReadyCards();
        gameRepository.save(initialGameContainer.getGame());
        initCards();
    }

    private void initCards() {
        cardInAnalysisBefore = initialGameContainer.getAnalysisPhase().getFirstColumn().getCards().get(0);
        cardInDevelopmentBefore = initialGameContainer.getDevelopmentPhase().getFirstColumn().getCards().get(0);
        cardInTestBefore = initialGameContainer.getTestPhase().getFirstColumn().getCards().get(0);
    }

    @When("^I change WIP limit of phase (.+) to (\\d+)$")
    public void i_change_wip_limit_of_phase_to(String phaseName, Integer wipLimit) throws Throwable {
        Phase phase = phaseRepository.findByName(phaseName);
        adjustWipLimitsAction.getPhaseWipLimits().put(phase.getId(), wipLimit);
    }

    @And("^I press the next round button$")
    public void i_press_the_next_round_button() throws Throwable {
        Turn turn = TurnBuilder.aTurn()
                .withAdjustWipLimitsAction(adjustWipLimitsAction)
                .build();
        request = request
                .contentType(APPLICATION_JSON_TYPE)
                .body(turn);
        response = request.when().put(GAME_PATH + PLAY_TURN_PATH).then();
    }

    @Then("^I should get a game with a turn played")
    public void i_should_get_a_game_with_a_turn_played() throws Throwable {
        response.statusCode(HttpURLConnection.HTTP_OK)
                .body(is(notNullValue()));
        response.extract().path("board.phases");
    }

    @And("^game should have phase (.+) with WIP limit (\\d+)$")
    public void game_should_have_phase_with_wip_limit(String phaseName, Integer wipLimit) throws Throwable {
        response.body("board.phases.find { it.name == '" + phaseName + "' }.wipLimit", equalTo(wipLimit));
    }

    @And("^the card in each work phase's first column has been worked on$")
    public void the_card_in_each_work_phases_first_column_has_been_worked_on() throws Throwable {
        throw new PendingException();
    }

    @And("^the card in each work phase's first column hasn't been moved to next column$")
    public void the_card_in_each_work_phases_first_column_hasnt_been_moved_to_next_column() throws Throwable {
        assertAnalysisCardIntact();
        assertDevelopmentCardIntact();
        assertTestCardIntact();
        assertDeployedEmpty();
    }

    private void assertAnalysisCardIntact() {
        response.body("board.phases.find { it.id == '" + ANALYSIS_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + ANALYSIS_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(cardInAnalysisBefore.getId()));
    }

    private void assertDevelopmentCardIntact() {
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(cardInDevelopmentBefore.getId()));
    }

    private void assertTestCardIntact() {
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(cardInTestBefore.getId()));
    }

    private void assertDeployedEmpty() {
        response.body("board.phases.find { it.id == '" + DEPLOYED_PHASE_ID + "' }.columns[0].cards.size()", equalTo(0));
    }

    @And("^the card in each work phase's first column has been moved to next column$")
    public void the_card_in_each_work_phases_first_column_has_been_moved_to_next_column() throws Throwable {
        assertAnalysisEmpty();
        assertAnalysisCardInDevelopment();
        assertDevelopmentCardInTest();
        assertTestCardInDeployed();
    }

    private void assertAnalysisEmpty() {
        response.body("board.phases.find { it.id == '" + ANALYSIS_PHASE_ID + "' }.columns[0].cards.size()", equalTo(0));
    }

    private void assertAnalysisCardInDevelopment() {
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(cardInAnalysisBefore.getId()));
    }

    private void assertDevelopmentCardInTest() {
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(cardInDevelopmentBefore.getId()));
    }

    private void assertTestCardInDeployed() {
        response.body("board.phases.find { it.id == '" + DEPLOYED_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + DEPLOYED_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(cardInTestBefore.getId()));
    }

    @And("^new card is drawn from backlog to the first column$")
    public void new_card_is_drawn_from_backlog_to_the_first_column() throws Throwable {
        throw new PendingException();
    }
}

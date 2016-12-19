package fi.aalto.ekanban.difficulty.normal.stepdefs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import static fi.aalto.ekanban.ApplicationConstants.*;

import java.net.HttpURLConnection;
import java.util.HashMap;

import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import fi.aalto.ekanban.SpringSteps;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.AdjustWipLimitsActionBuilder;
import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
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
    private Card firstCardInAnalysisBefore;
    private Card firstCardInDevelopmentBefore;
    private Card firstCardInTestBefore;
    private Integer cardsInBacklogDeckBefore;
    private Integer wipLimitOfAnalysisAfter;

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
        saveSituationBefore();
    }

    @And("^game has one ready card in first columns of the work phases$")
    public void game_has_one_ready_card_in_first_columns_of_the_work_phases() throws Throwable {
        initialGameContainer.fillFirstWorkPhasesWithSomeReadyCards();
        initialGameContainer.fillLastWorkPhaseWithSomeReadyCards();
        gameRepository.save(initialGameContainer.getGame());
        saveSituationBefore();
    }

    @And("^it has wip limit of (\\d+) in phase (.+)")
    public void it_has_wip_limit_in_phase(Integer wipLimit, String phaseName) throws Throwable {
        if (phaseName.equals("Analysis")) {
            initialGameContainer.getAnalysisPhase().setWipLimit(wipLimit);
            gameRepository.save(initialGameContainer.getGame());
            saveSituationBefore();
        }
        else {
            throw new UnsupportedOperationException("That phase not implemented yet");
        }
    }

    @And("^one turn has already been played$")
    public void one_turn_has_already_been_played() throws Throwable {
        i_press_the_next_round_button();
        String gameId = initialGameContainer.getGame().getId();
        Game updatedGame = gameRepository.findOne(gameId);
        initialGameContainer = TestGameContainer.withGame(updatedGame);
        saveSituationBefore();
    }

    private void saveSituationBefore() {
        if (initialGameContainer.getAnalysisPhase().getFirstColumn().getCards().size() >= 1) {
            firstCardInAnalysisBefore = initialGameContainer.getAnalysisPhase().getFirstColumn().getCards().get(0);
        }
        if (initialGameContainer.getDevelopmentPhase().getFirstColumn().getCards().size() >= 1) {
            firstCardInDevelopmentBefore = initialGameContainer.getDevelopmentPhase()
                    .getFirstColumn().getCards().get(0);
        }
        if (initialGameContainer.getTestPhase().getFirstColumn().getCards().size() >= 1) {
            firstCardInTestBefore = initialGameContainer.getTestPhase().getFirstColumn().getCards().get(0);
        }
        cardsInBacklogDeckBefore = initialGameContainer.getGame().getBoard().getBacklogDeck().size();

        // saving this already in case it's not being changed by a when-action
        wipLimitOfAnalysisAfter = initialGameContainer.getAnalysisPhase().getWipLimit();
    }

    @When("^I change WIP limit of phase (.+) to (-?\\d+)$")
    public void i_change_wip_limit_of_phase_to(String phaseName, Integer wipLimit) throws Throwable {
        Phase phase = phaseRepository.findByName(phaseName);
        adjustWipLimitsAction.getPhaseWipLimits().put(phase.getId(), wipLimit);
        if (phase.getId().equals(ANALYSIS_PHASE_ID)) {
            wipLimitOfAnalysisAfter = wipLimit;
        }
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
    }

    @Then("^I should get an error$")
    public void i_should_get_an_error() throws Throwable {
        response.statusCode(not(HttpURLConnection.HTTP_OK));
    }

    @And("^game should have phase (.+) with WIP limit (\\d+)$")
    public void game_should_have_phase_with_wip_limit(String phaseName, Integer wipLimit) throws Throwable {
        response.body("board.phases.find { it.name == '" + phaseName + "' }.wipLimit", equalTo(wipLimit));
    }

    @And("^the card in each work phase's first column has been worked on$")
    public void the_card_in_each_work_phases_first_column_has_been_worked_on() throws Throwable {
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards[0]"
                        + ".cardPhasePoints.find { it.phaseId == '" + ANALYSIS_PHASE_ID + "' }.pointsDone",
                equalTo(10));
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards[0]"
                        + ".cardPhasePoints.find { it.phaseId == '" + DEVELOPMENT_PHASE_ID + "' }.pointsDone",
                equalTo(10));
        response.body("board.phases.find { it.id == '" + DEPLOYED_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + DEPLOYED_PHASE_ID + "' }.columns[0].cards[0]"
                        + ".cardPhasePoints.find { it.phaseId == '" + TEST_PHASE_ID + "' }.pointsDone",
                equalTo(10));
    }

    @And("^the card in each work phase's first column has been moved to next phase$")
    public void the_card_in_each_work_phases_first_column_has_been_moved_to_next_phase() throws Throwable {
        assertAnalysisCardInDevelopment();
        assertDevelopmentCardInTest();
        assertTestCardInDeployed();
    }

    private void assertAnalysisCardInDevelopment() {
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(firstCardInAnalysisBefore.getId()));
    }

    private void assertDevelopmentCardInTest() {
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + TEST_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(firstCardInDevelopmentBefore.getId()));
    }

    private void assertTestCardInDeployed() {
        response.body("board.phases.find { it.id == '" + DEPLOYED_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(1));
        response.body("board.phases.find { it.id == '" + DEPLOYED_PHASE_ID + "' }.columns[0].cards[0].id",
                equalTo(firstCardInTestBefore.getId()));
    }

    @And("^new cards are drawn from backlog to the first column$")
    public void new_cards_are_drawn_from_backlog_to_the_first_column() throws Throwable {
        response.body("board.backlogDeck.size()", equalTo(cardsInBacklogDeckBefore - wipLimitOfAnalysisAfter));
        response.body("board.phases.find { it.id == '" + ANALYSIS_PHASE_ID + "' }.columns[0].cards.size()",
                equalTo(wipLimitOfAnalysisAfter));
        response.body("board.phases.find { it.id == '" + ANALYSIS_PHASE_ID + "' }.columns[0].cards[0]"
                        + ".cardPhasePoints.find { it.phaseId == '" + ANALYSIS_PHASE_ID + "' }.pointsDone",
                equalTo(0));
    }

    @And("^the phase (.+) should have some cards in its first column$")
    public void the_phase_should_have_some_cards_in_its_first_column(String phaseName) throws Throwable {
        response.body("board.phases.find { it.name == '" + phaseName + "' }.columns[0].cards.size()",
                greaterThan(0));
    }

    @And("^the phase (.+) should still have more cards than it's WIP limit$")
    public void the_phase_should_still_have_more_cards_than_its_wip_limit(String phaseName) throws Throwable {
        response.body("board.phases.find { it.name == '" + phaseName + "' }.columns[0].cards.size()",
                greaterThan(wipLimitOfAnalysisAfter));
    }
}

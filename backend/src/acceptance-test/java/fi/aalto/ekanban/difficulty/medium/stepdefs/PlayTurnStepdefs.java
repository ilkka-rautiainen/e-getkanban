package fi.aalto.ekanban.difficulty.medium.stepdefs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static fi.aalto.ekanban.ApplicationConstants.*;
import static fi.aalto.ekanban.AcceptanceTestUtil.getIndexInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.SpringSteps;
import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.phases.Column;
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
    private ValidatableResponse response;

    private Game updatedGame;
    private AdjustWipLimitsAction adjustWipLimitsAction;
    private List<AssignResourcesAction> assignResourcesActions;

    @Given("^I have a game with difficulty of Medium$")
    public void i_have_a_game_with_difficulty_of_medium() {
        initGame();
        initRequest();
    }

    private void initGame() {
        initialGameContainer = TestGameContainer.withMediumDifficultyMockGame();
    }

    private void initRequest() {
        request = given()
                .pathParam("gameId", initialGameContainer.getGame().getId());
        adjustWipLimitsAction = AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction()
                .withPhaseWipLimits(new HashMap<>())
                .build();
        assignResourcesActions = new ArrayList<>();
    }

    @Given("^the game has (-?\\d+) cards in (.+) column of (.+) phase$")
    public void the_game_has_cards_in_column_of_phase(int cardCount, String column, String phase) {
        List<Card> phaseCards = new ArrayList<>();
        Column columnToAddCards = initialGameContainer.getGame().getBoard()
                .getPhaseWithId(phase.toUpperCase()).getColumns().get(getIndexInt(column));
        for (int i = 0; i < cardCount; i++) {
            phaseCards.add(CardBuilder.aCard().withMockPhasePoints().build());
        }
        columnToAddCards.setCards(phaseCards);
    }

    @Given("^first card needs (\\d+) points in Analysis to be done$")
    public void first_card_needs_points_in_Analysis_to_be_done(int neededPointAmount) {
        initialGameContainer.getGame().getBoard()
                .getPhaseWithId(ANALYSIS_PHASE_ID).getColumns().get(0).getCards().get(0)
                .getCardPhasePointOfPhase(ANALYSIS_PHASE_ID).setTotalPoints(neededPointAmount);
    }

    @Given("^the game has been played for (\\d+) rounds already so that I've resource dice at my disposal$")
    public void the_game_has_been_played_for_rounds_already_so_that_I_ve_resource_dice_at_my_disposal(int roundNumber) {
        initialGameContainer.getGame().setCurrentDay(roundNumber);
        initialGameContainer.fillCFDWithZeroValuesUntilDay(roundNumber);
        gameRepository.save(initialGameContainer.getGame());
    }

    @When("^I assign the (.+) resource dice of (.+) phase to the first card in (.+) phase$")
    public void i_assign_the_resource_dice_of_phase_to_the_first_card_in_phase(String dieIndex, String diePhase, String cardPhase) {
        Column columnOfCard = initialGameContainer.getGame().getBoard()
                .getPhaseWithId(cardPhase.toUpperCase()).getColumns().get(0);
        Card firstCard = columnOfCard.getCards().get(0);
        AssignResourcesAction assignResourcesAction =
                AssignResourcesActionBuilder.anAssignResourcesAction()
                        .withCardId(firstCard.getId())
                        .withCardPhaseId(cardPhase.toUpperCase())
                        .withDieIndex(getIndexInt(dieIndex))
                        .withDiePhaseId(diePhase.toUpperCase())
                        .build();
        assignResourcesActions.add(assignResourcesAction);
    }

    @When("^I change WIP limit of phase (.+) to (-?\\d+)$")
    public void i_change_wip_limit_of_phase_to(String phaseName, Integer wipLimit) {
        Phase phase = phaseRepository.findByName(phaseName);
        adjustWipLimitsAction.getPhaseWipLimits().put(phase.getId(), wipLimit);
    }

    @And("^I press the next round button$")
    public void i_press_the_next_round_button() {
        Turn turn = TurnBuilder.aTurn()
                .withAdjustWipLimitsAction(adjustWipLimitsAction)
                .withAssignResourcesActions(assignResourcesActions)
                .build();
        request = request
                .contentType(APPLICATION_JSON_TYPE)
                .body(turn);
        Response resp = request.when().put(GAME_PATH + PLAY_TURN_PATH);
        System.out.println(resp.prettyPrint());
        response = resp.then();
        updatedGame = gameRepository.findOne(initialGameContainer.getGame().getId());
    }

    @Then("^first card in (.+) phase should have the (.+) secondary dice value from the (.+) die of (.+) phase$")
    public void first_card_in_phase_should_have_the_first_secondary_dice_value_from_the_first_die_of_phase(String cardPhase,
               String dieSecondaryValue, String dieNumber, String diePhase) {
        Column columnOfCard = updatedGame.getBoard()
                .getPhaseWithId(cardPhase.toUpperCase()).getColumns().get(0);
        Card firstCard = columnOfCard.getCards().get(0);
        String dieValue = dieSecondaryValue+"SecondaryValue";

        response.body("lastTurn.diceCastActions.find { it.phaseId == '" + diePhase.toUpperCase() + "' }" +
                        ".dice["+getIndexInt(dieNumber)+"]."+dieValue,
                equalTo(firstCard.getCardPhasePointOfPhase(cardPhase.toUpperCase()).getPointsDone()));
    }

    @Then("^second card in Development has not been worked on$")
    public void second_card_in_Development_has_not_been_worked_on() {
        response.body("board.phases.find { it.id == '" + DEVELOPMENT_PHASE_ID + "' }.columns[0].cards[1]"
                        + ".cardPhasePoints.find { it.phaseId == '" + DEVELOPMENT_PHASE_ID + "' }.pointsDone",
                equalTo(0));
    }

    @Then("^In progress column of Analysis should hold only one card$")
    public void in_progress_column_of_Analysis_should_hold_only_one_card() {
        response.body("board.phases.find { it.id == '" + ANALYSIS_PHASE_ID + "' }.columns[0].cards.size()", equalTo(1));
    }

    @Then("^first card in Analysis phase should have the dice primary values summed in Analysis resource points$")
    public void first_card_in_Analysis_phase_should_have_the_dice_primary_values_summed_in_Analysis_resource_points() {
        Integer firstDiceValue = response.extract().path("lastTurn.diceCastActions[0].dice[0].primaryValue");
        Integer secondDiceValue = response.extract().path("lastTurn.diceCastActions[0].dice[1].primaryValue");
        response.body("board.phases.find { it.id == '" + ANALYSIS_PHASE_ID + "' }.columns[0].cards[0].cardPhasePoints[0].pointsDone",
                equalTo(firstDiceValue+secondDiceValue));
    }

}

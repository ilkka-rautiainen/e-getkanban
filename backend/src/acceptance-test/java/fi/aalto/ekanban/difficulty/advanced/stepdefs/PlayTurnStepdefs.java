package fi.aalto.ekanban.difficulty.advanced.stepdefs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;
import static fi.aalto.ekanban.ApplicationConstants.*;
import static fi.aalto.ekanban.AcceptanceTestUtil.getIndexInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.SpringSteps;
import fi.aalto.ekanban.builders.AdjustWipLimitsActionBuilder;
import fi.aalto.ekanban.builders.AssignResourcesActionBuilder;
import fi.aalto.ekanban.builders.CardBuilder;
import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.models.AdjustWipLimitsAction;
import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
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

    @Given("^I have a game with difficulty of Advanced$")
    public void i_have_a_game_with_difficulty_of_advanced() {
        initGame();
        initRequest();
    }

    private void initGame() {
        initialGameContainer = TestGameContainer.withAdvancedDifficultyMockGame();
    }

    private void initRequest() {
        request = given()
                .pathParam("gameId", initialGameContainer.getGame().getId());
        adjustWipLimitsAction = AdjustWipLimitsActionBuilder.anAdjustWipLimitsAction()
                .withPhaseWipLimits(new HashMap<>())
                .build();
        assignResourcesActions = new ArrayList<>();
    }

    @Given("^(.+) column of Analysis has (\\d+) cards$")
    public void in_Progress_column_of_Analysis_has_cards(String columnName, int inProgressCardCount) {
        Integer columnIndex = columnName.equals("In Progress") ? 0 : 1;
        List<Card> phaseCards = new ArrayList<>();
        Column columnToAddCards = initialGameContainer.getGame().getBoard()
                .getPhaseWithId(ANALYSIS_PHASE_ID).getColumns().get(columnIndex);
        for (int i = 0; i < inProgressCardCount; i++) {
            phaseCards.add(CardBuilder.aCard().withMockPhasePoints().build());
        }
        columnToAddCards.setCards(phaseCards);
    }

    @Given("^(.+) phase has (\\d+) cards$")
    public void phase_has_cards(String phaseName, int cardCount) {
        List<Card> phaseCards = new ArrayList<>();
        for (int i = 0; i < cardCount; i++) {
            phaseCards.add(CardBuilder.aCard().withMockPhasePoints().withDayStarted(1).build());
        }
        initialGameContainer.getGame().getBoard().getPhaseWithId(phaseName.toUpperCase()).getFirstColumn().setCards(phaseCards);
    }

    @Given("^the current day of the game is (\\d+)$")
    public void the_current_day_of_the_game_is(Integer currentDay) throws Throwable {
        initialGameContainer.getGame().setCurrentDay(currentDay);
        initialGameContainer.fillCFDWithZeroValuesUntilDay(currentDay);
        gameRepository.save(initialGameContainer.getGame());
    }

    @When("^I change WIP limit of phase (.+) to (-?\\d+)$")
    public void i_change_wip_limit_of_phase_to(String phaseName, Integer wipLimit) {
        Phase phase = phaseRepository.findByName(phaseName);
        adjustWipLimitsAction.getPhaseWipLimits().put(phase.getId(), wipLimit);
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

    @When("^I press the next round button$")
    public void i_press_the_next_round_button() {
        Turn turn = TurnBuilder.aTurn()
                .withAdjustWipLimitsAction(adjustWipLimitsAction)
                .withAssignResourcesActions(assignResourcesActions)
                .build();
        request = request
                .contentType(APPLICATION_JSON_TYPE)
                .body(turn);
        response = request.when().put(GAME_PATH + PLAY_TURN_PATH).then();
        updatedGame = gameRepository.findOne(initialGameContainer.getGame().getId());
    }

    @Then("^game should have current day of (\\d+)$")
    public void gameShouldHaveCurrentDayOf(int currentDay) {
        response.body("currentDay", equalTo(currentDay));
    }

    @Then("^(.+) phase should contain (\\d+) cards$")
    public void analysis_phase_should_contain_cards(String phaseName, int analysisCardCount) {
        response.body("board.phases.find { it.id == '" + phaseName.toUpperCase() + "' }.columns.cards.flatten().size()",
                equalTo(analysisCardCount));
    }

}

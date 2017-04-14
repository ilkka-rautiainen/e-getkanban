package fi.aalto.ekanban.services.turnplayers;

import static fi.aalto.ekanban.ApplicationConstants.DAY_THRESHOLD_TO_RETURN_DICE_CAST_ACTIONS;
import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.TEST_PHASE_ID;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.services.ai.assignresources.DiceCastService;
import fi.aalto.ekanban.services.ai.drawfrombacklog.DrawFromBacklogAIService;
import fi.aalto.ekanban.services.ai.movecards.MoveCardsAIService;
import fi.aalto.ekanban.services.ActionExecutorService;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.*;

@Service
public class MediumTurnPlayerService implements TurnPlayer {

    @Autowired
    private ActionExecutorService actionExecutorService;

    @Autowired
    private MoveCardsAIService moveCardsAIService;

    @Autowired
    private DrawFromBacklogAIService drawFromBacklogAIService;

    @Autowired
    private DiceCastService diceCastService;

    public Game playTurn(Game game, Turn turn) {
        game.setLastTurn(turn);
        game = actionExecutorService.adjustWipLimits(game, turn.getAdjustWipLimitsAction());
        game = assignResources(game, turn.getAssignResourcesActions());
        game = moveCardsWithAI(moveCardsAIService, actionExecutorService, game);
        game = drawFromBacklogWithAI(drawFromBacklogAIService, actionExecutorService, game);
        return game;
    }

    private Game assignResources(Game game, List<AssignResourcesAction> assignResourceActions) {
        List<DiceCastAction> diceCastActions = diceCastService.getDiceCastActions(game);
        for (AssignResourcesAction assignResourcesAction : assignResourceActions) {
            DiceCastAction phaseDiceCastAction = diceCastActions.stream()
                    .filter(action -> action.getPhaseId().equals(assignResourcesAction.getDiePhaseId()))
                    .collect(Collectors.toList()).get(0);
            assignPointsForResourceAction(assignResourcesAction, phaseDiceCastAction);
        }
        game = actionExecutorService.assignResources(game, assignResourceActions);
        game.getLastTurn().setAssignResourcesActions(assignResourceActions);
        if (game.getCurrentDay() >= DAY_THRESHOLD_TO_RETURN_DICE_CAST_ACTIONS) {
            game.getLastTurn().setDiceCastActions(diceCastActions);
        }
        return game;
    }

    private void assignPointsForResourceAction(AssignResourcesAction assignResourcesAction, DiceCastAction phaseDiceCastAction) {
        if (assignResourcesAction.getDiePhaseId().equals(assignResourcesAction.getCardPhaseId())) {
            assignResourcesAction.setPoints(
                    phaseDiceCastAction.getDice().get(assignResourcesAction.getDieIndex()).getPrimaryValue()
            );
        } else {
            if (setCorrectPoints(assignResourcesAction, phaseDiceCastAction, ANALYSIS_PHASE_ID, DEVELOPMENT_PHASE_ID, TEST_PHASE_ID))
                return;
            if (setCorrectPoints(assignResourcesAction, phaseDiceCastAction, DEVELOPMENT_PHASE_ID, ANALYSIS_PHASE_ID, TEST_PHASE_ID))
                return;
            setCorrectPoints(assignResourcesAction, phaseDiceCastAction, TEST_PHASE_ID, ANALYSIS_PHASE_ID, DEVELOPMENT_PHASE_ID);
        }
    }

    private Boolean setCorrectPoints(AssignResourcesAction assignResourcesAction, DiceCastAction phaseDiceCastAction,
                        String cardPhaseId, String firstDiePhaseId, String secondDiePhaseId) {
        if (assignResourcesAction.getDiePhaseId().equals(cardPhaseId)) {
            if (assignResourcesAction.getCardPhaseId().equals(firstDiePhaseId)) {
                assignResourcesAction.setPoints(
                        phaseDiceCastAction.getDice().get(assignResourcesAction.getDieIndex()).getFirstSecondaryValue()
                );
            }
            else if (assignResourcesAction.getCardPhaseId().equals(secondDiePhaseId)) {
                assignResourcesAction.setPoints(
                        phaseDiceCastAction.getDice().get(assignResourcesAction.getDieIndex()).getSecondSecondaryValue()
                );
            }
            return true;
        }
        return false;
    }

}

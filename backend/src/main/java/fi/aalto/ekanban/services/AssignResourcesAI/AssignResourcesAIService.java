package fi.aalto.ekanban.services.AssignResourcesAI;

import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_MAX;
import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_MIN;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.builders.AssignResourcesActionBuilder;
import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Phase;

@Service
public class AssignResourcesAIService {

    private DiceService diceService;

    @Autowired
    public AssignResourcesAIService(DiceService diceService) {
        this.diceService = diceService;
    }

    public List<AssignResourcesAction> getAssignResourcesActions(Game game) {
        List<AssignResourcesAction> assignResourcesActions = new ArrayList<>();
        List<Phase> phases = game.getBoard().getWorkPhases();
        for (Phase phase : phases) {
            List<AssignResourcesAction> actions = getAssignResourcesActions(phase);
            assignResourcesActions.addAll(actions);
        }
        return assignResourcesActions;
    }

    private List<AssignResourcesAction> getAssignResourcesActions(Phase phase) {
        List<AssignResourcesAction> actions = new ArrayList<>();
        Integer diceValueLeft = getTotalDiceRandomValue(phase.getDiceAmount());
        List<Card> cardsReversed = Lists.reverse(phase.getFirstColumn().getCards());
        for (Integer i = 0; i < cardsReversed.size() && diceValueLeft > 0; i++) {
            Card card = cardsReversed.get(i);
            CardPhasePoint cardPhasePoint = card.getCardPhasePointOfPhase(phase.getId());
            if (cardPhasePoint.isReady()) {
                continue;
            }
            Integer pointsUntilDone = cardPhasePoint.pointsUntilDone();
            Integer pointsToUse = Math.min(pointsUntilDone, diceValueLeft);
            diceValueLeft -= pointsToUse;
            AssignResourcesAction action = AssignResourcesActionBuilder.anAssignResourcesAction()
                    .withCardId(card.getId())
                    .withPhaseId(phase.getId())
                    .withPoints(pointsToUse)
                    .build();
            actions.add(action);
        }
        return actions;
    }

    private Integer getTotalDiceRandomValue(Integer diceAmount) {
        Integer diceNumber = 0;
        for (Integer i = 0; i < diceAmount; i++) {
            diceNumber += diceService.cast(RESOURCE_DICE_MIN, RESOURCE_DICE_MAX);
        }
        return diceNumber;
    }
}

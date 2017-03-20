package fi.aalto.ekanban.services.ai.assignresources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fi.aalto.ekanban.builders.AssignResourcesActionBuilder;
import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.DiceCastAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Phase;

@Service
public class AssignResourcesAIService {

    public AssignResourcesAIService() {
    }

    public List<AssignResourcesAction> getAssignResourcesActions(Game game, List<DiceCastAction> diceCastActions) {
        List<AssignResourcesAction> assignResourcesActions = new ArrayList<>();
        for (DiceCastAction diceCastAction : diceCastActions) {
            Phase phase = game.getBoard().getPhaseWithId(diceCastAction.getPhaseId());
            List<AssignResourcesAction> actions = getAssignResourcesActions(phase, diceCastAction);
            assignResourcesActions.addAll(actions);
        }
        return assignResourcesActions;
    }

    private List<AssignResourcesAction> getAssignResourcesActions(Phase phase, DiceCastAction diceCastAction) {
        List<AssignResourcesAction> actions = new ArrayList<>();
        Integer diceValueLeft = diceCastAction.getTotalDiceValue();
        List<Card> cards = phase.getFirstColumn().getCards();
        for (Integer i = 0; i < cards.size() && diceValueLeft > 0; i++) {
            Card card = cards.get(i);
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
}

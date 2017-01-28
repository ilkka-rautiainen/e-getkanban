package fi.aalto.ekanban.services.ai.assignresources;

import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_MAX;
import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_MIN;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.builders.DiceCastActionBuilder;
import fi.aalto.ekanban.models.DiceCastAction;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.services.dice.DiceService;

@Service
public class DiceCastService {

    private DiceService diceService;

    @Autowired
    public DiceCastService(DiceService diceService) {
        this.diceService = diceService;
    }

    public List<DiceCastAction> getDiceCastActions(Game game) {
        List<DiceCastAction> diceCastActions = new ArrayList<>();
        game.getBoard().getWorkPhases().forEach(workPhase -> {
            List<Integer> diceValues = getDiceValues(workPhase.getDiceAmount());
            diceCastActions.add(DiceCastActionBuilder.aDiceCastAction()
                    .withPhaseId(workPhase.getId())
                    .withDiceValues(diceValues)
                    .build());
        });
        return diceCastActions;
    }

    private List<Integer> getDiceValues(Integer diceAmount) {
        List<Integer> diceValues = new ArrayList<>();
        for (Integer i = 0; i < diceAmount; i++) {
            Integer diceValue = diceService.castDie(RESOURCE_DICE_MIN, RESOURCE_DICE_MAX);
            diceValues.add(diceValue);
        }
        return diceValues;
    }

}

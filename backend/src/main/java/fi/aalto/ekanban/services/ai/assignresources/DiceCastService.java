package fi.aalto.ekanban.services.ai.assignresources;

import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_PRIMARY_MAX;
import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_PRIMARY_MIN;
import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_SECONDARY_MAX;
import static fi.aalto.ekanban.ApplicationConstants.RESOURCE_DICE_SECONDARY_MIN;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.builders.DiceCastActionBuilder;
import fi.aalto.ekanban.models.DiceCastAction;
import fi.aalto.ekanban.models.DieBuilder;
import fi.aalto.ekanban.builders.Die;
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
        Stream<Phase> phaseStream = game.getBoard().getWorkPhases().stream();
        if (game.getDifficultyLevel().equals(GameDifficulty.NORMAL)) {
             phaseStream = phaseStream
                    .filter(workPhase -> !workPhase.getFirstColumn().getCards().isEmpty());
        }
        phaseStream.forEach(workPhase -> {
            List<Die> dice = getDice(workPhase.getDiceAmount());
            diceCastActions.add(DiceCastActionBuilder.aDiceCastAction()
                    .withPhaseId(workPhase.getId())
                    .withDice(dice)
                    .build());
        });
        return diceCastActions;
    }

    private List<Die> getDice(Integer diceAmount) {
        List<Die> dice = new ArrayList<>();
        for (Integer i = 0; i < diceAmount; i++) {
            Integer primaryValue = diceService.castDie(RESOURCE_DICE_PRIMARY_MIN, RESOURCE_DICE_PRIMARY_MAX);
            Integer firstSecondaryValue = diceService.castDie(RESOURCE_DICE_SECONDARY_MIN, RESOURCE_DICE_SECONDARY_MAX);
            Integer secondSecondaryValue = diceService.castDie(RESOURCE_DICE_SECONDARY_MIN, RESOURCE_DICE_SECONDARY_MAX);
            dice.add(DieBuilder.aDie()
                    .withPrimaryValue(primaryValue)
                    .withFirstSecondaryValue(firstSecondaryValue)
                    .withSecondSecondaryValue(secondSecondaryValue)
                    .build());
        }
        return dice;
    }

}

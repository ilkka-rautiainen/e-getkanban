package fi.aalto.ekanban.services.ai.movecards;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.builders.MoveCardActionBuilder;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

@Service
public class MoveCardsAIService {
    private Game game;
    private List<Phase> phasesReversed;
    private static final Logger logger = LoggerFactory.getLogger(MoveCardsAIService.class);

    public List<MoveCardAction> getMoveCardsActions(Game game) {
        this.game = game;
        this.phasesReversed = Lists.reverse(game.getBoard().getPhases());
        return getMoveCardActionsInWorkPhases();
    }

    private List<MoveCardAction> getMoveCardActionsInWorkPhases() {
        List<Phase> workPhases = phasesReversed.stream()
                .filter(Phase::getIsWorkPhase)
                .collect(Collectors.toList());
        List<MoveCardAction> moveCardActions = new ArrayList<>();
        Integer cardsLeftOutFromLatestPhase = 0;
        for (Phase phase : workPhases) {
            PhaseActions latestPhaseActions = getMoveCardActionsInPhase(phase, cardsLeftOutFromLatestPhase);
            cardsLeftOutFromLatestPhase = latestPhaseActions.getCardsLetOutOfPhase();
            moveCardActions.addAll(latestPhaseActions.getMoveCardActions());
        }
        return moveCardActions;
    }

    private PhaseActions getMoveCardActionsInPhase(Phase currentPhase, Integer cardsLeftOutFromLatestPhase) {
        List<MoveCardAction> secondColumnActionsToNextPhase = actionsFromSecondColumnToNextPhase(currentPhase,
                cardsLeftOutFromLatestPhase);
        Integer cardsLetOutFromSecondColumnToNextPhase = secondColumnActionsToNextPhase.size();
        PhaseActions firstColumnActions = actionsFromFirstColumn(currentPhase, cardsLeftOutFromLatestPhase,
                cardsLetOutFromSecondColumnToNextPhase);

        List<MoveCardAction> bothColumnActions = new ArrayList<>();
        bothColumnActions.addAll(secondColumnActionsToNextPhase);
        bothColumnActions.addAll(firstColumnActions.getMoveCardActions());

        Integer cardsLetOutTotally = firstColumnActions.getCardsLetOutOfPhase()
                + cardsLetOutFromSecondColumnToNextPhase;

        return PhaseActionsBuilder.aPhaseActions()
                .withCardsLetOutOfPhase(cardsLetOutTotally)
                .withMoveCardActions(bothColumnActions)
                .build();
    }

    private PhaseActions actionsFromFirstColumn(Phase currentPhase, Integer cardsLeftOutFromLatestPhase,
                                                Integer cardsAlreadyMovedToLatestPhase) {
        List<MoveCardAction> actionsFromFirstColumnToNextPhase = actionsFromFirstColumnToNextPhase(currentPhase,
                cardsLeftOutFromLatestPhase, cardsAlreadyMovedToLatestPhase);
        Integer cardsLetOutFromFirstColumnToNextPhase = actionsFromFirstColumnToNextPhase.size();
        List<MoveCardAction> actionsFromFirstColumnToSecond
                = actionsFromFirstColumnToSecond(currentPhase, cardIdsFrom(actionsFromFirstColumnToNextPhase));

        List<MoveCardAction> firstColumnActions = new ArrayList<>();
        firstColumnActions.addAll(actionsFromFirstColumnToNextPhase);
        firstColumnActions.addAll(actionsFromFirstColumnToSecond);

        return PhaseActionsBuilder.aPhaseActions()
                .withCardsLetOutOfPhase(cardsLetOutFromFirstColumnToNextPhase)
                .withMoveCardActions(firstColumnActions)
                .build();
    }

    private List<String> cardIdsFrom(List<MoveCardAction> actions) {
        return actions.stream()
                .map(MoveCardAction::getCardId)
                .collect(Collectors.toList());
    }

    private List<MoveCardAction> actionsFromSecondColumnToNextPhase(Phase currentPhase,
                                                                    Integer cardsLeftOutFromLatestPhase) {
        if (currentPhase.hasSecondColumn() && game.hasNextPhase(currentPhase)) {
            Column secondColumn = currentPhase.getSecondColumn();
            Phase nextPhase = game.getNextPhase(currentPhase);
            Integer cardsAlreadyMovedToLatestPhase = 0;
            return actionsToNextPhaseFromLastColumnOfPhase(currentPhase, secondColumn, nextPhase,
                    cardsLeftOutFromLatestPhase, cardsAlreadyMovedToLatestPhase);
        }
        return new ArrayList<>();
    }

    private List<MoveCardAction> actionsFromFirstColumnToNextPhase(Phase currentPhase,
                                                                   Integer cardsLeftOutFromLatestPhase,
                                                                   Integer cardsAlreadyMovedToLatestPhase) {
        if (game.hasNextPhase(currentPhase)) {
            Column firstColumn = currentPhase.getFirstColumn();
            Phase nextPhase = game.getNextPhase(currentPhase);
            if (currentPhase.hasSecondColumn()) {
                Column secondColumn = currentPhase.getSecondColumn();
                return actionsToNextPhaseOverSecondColumn(currentPhase, firstColumn, secondColumn, nextPhase,
                        cardsLeftOutFromLatestPhase, cardsAlreadyMovedToLatestPhase);
            }
            else {
                return actionsToNextPhaseFromLastColumnOfPhase(currentPhase, firstColumn, nextPhase,
                        cardsLeftOutFromLatestPhase, cardsAlreadyMovedToLatestPhase);
            }
        }
        return new ArrayList<>();
    }

    private List<MoveCardAction> actionsToNextPhaseFromLastColumnOfPhase(Phase currentPhase, Column fromColumn,
                                                                         Phase nextPhase,
                                                                         Integer cardsLeftOutFromLatestPhase,
                                                                         Integer cardsAlreadyMovedToLatestPhase) {
        List<Card> columnCardsReversed = Lists.reverse(fromColumn.getCards());
        Integer cardAmountToMoveToNextPhase = getCardAmountToMoveToNextPhase(nextPhase, columnCardsReversed,
                cardsLeftOutFromLatestPhase, cardsAlreadyMovedToLatestPhase);

        return columnCardsReversed.stream()
                .limit(cardAmountToMoveToNextPhase)
                .filter(card -> card.isReadyWithPhase(currentPhase))
                .map(cardToMove -> MoveCardActionBuilder.aMoveCardAction()
                        .withCardId(cardToMove.getId())
                        .withFromColumnId(fromColumn.getId())
                        .withToColumnId(nextPhase.getFirstColumn().getId())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MoveCardAction> actionsToNextPhaseOverSecondColumn(Phase currentPhase, Column firstColumn,
                                                                    Column secondColumn, Phase nextPhase,
                                                                    Integer cardsLeftOutFromLatestPhase,
                                                                    Integer cardsAlreadyMovedToLatestPhase) {
        List<Card> columnCardsReversed = Lists.reverse(firstColumn.getCards());
        Integer cardAmountToMoveToNextPhase = getCardAmountToMoveToNextPhase(nextPhase, columnCardsReversed,
                cardsLeftOutFromLatestPhase, cardsAlreadyMovedToLatestPhase);

        return columnCardsReversed.stream()
                .limit(cardAmountToMoveToNextPhase)
                .filter(card -> card.isReadyWithPhase(currentPhase))
                .map(cardToMove -> Arrays.asList(
                        MoveCardActionBuilder.aMoveCardAction()
                                .withCardId(cardToMove.getId())
                                .withFromColumnId(firstColumn.getId())
                                .withToColumnId(secondColumn.getId())
                                .build(),
                        MoveCardActionBuilder.aMoveCardAction()
                                .withCardId(cardToMove.getId())
                                .withFromColumnId(secondColumn.getId())
                                .withToColumnId(nextPhase.getFirstColumn().getId())
                                .build()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private Integer getCardAmountToMoveToNextPhase(Phase nextPhase, List<Card> columnCardsReversed,
                                                   Integer cardsLeftOutFromLatestPhase,
                                                   Integer cardsAlreadyMovedToLatestPhase) {
        Integer cardAmountToMoveToNextPhase;
        Integer cardsInColumn = columnCardsReversed.size();
        if (nextPhase.getWipLimit() == null) {
            cardAmountToMoveToNextPhase = cardsInColumn;
        }
        else {
            Integer placesInNextPhase = placesInPhase(nextPhase, cardsLeftOutFromLatestPhase,
                    cardsAlreadyMovedToLatestPhase);
            cardAmountToMoveToNextPhase = Math.min(placesInNextPhase, cardsInColumn);
        }
        return cardAmountToMoveToNextPhase;
    }

    private List<MoveCardAction> actionsFromFirstColumnToSecond(Phase currentPhase,
                                                                List<String> cardsIdsTakenFromFirstColumnToNextPhase) {
        Column firstColumn = currentPhase.getFirstColumn();
        List<Card> firstColumnCardsReversed = Lists.reverse(firstColumn.getCards());
        Integer totalCardsInFirstColumn = firstColumnCardsReversed.size();
        Integer cardsLeft = totalCardsInFirstColumn - cardsIdsTakenFromFirstColumnToNextPhase.size();

        if (currentPhase.hasSecondColumn() && cardsLeft > 0) {
            Column secondColumn = currentPhase.getSecondColumn();
            return firstColumnCardsReversed.stream()
                    .filter(card -> card.isReadyWithPhase(currentPhase)
                            && !cardsIdsTakenFromFirstColumnToNextPhase.contains(card.getId()))
                    .map(cardToMove -> MoveCardActionBuilder.aMoveCardAction()
                            .withCardId(cardToMove.getId())
                            .withFromColumnId(firstColumn.getId())
                            .withToColumnId(secondColumn.getId())
                            .build())
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    private Integer placesInPhase(Phase phase, Integer cardsLeftOutFromLatestPhase,
                                  Integer cardsAlreadyMovedToLatestPhase) {
        Integer normalPlaces = Math.max(phase.getWipLimit() - phase.getTotalAmountOfCards(), 0);
        return normalPlaces + cardsLeftOutFromLatestPhase - cardsAlreadyMovedToLatestPhase;
    }
}

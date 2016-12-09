package fi.aalto.ekanban.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.exceptions.*;
import fi.aalto.ekanban.models.*;
import fi.aalto.ekanban.models.db.games.*;
import fi.aalto.ekanban.models.db.phases.*;
import fi.aalto.ekanban.services.AssignResourcesAI.AssignResourcesAIService;
import fi.aalto.ekanban.services.MoveCardsAI.MoveCardsAIService;

@Service
public class PlayerService {

    @Autowired
    AssignResourcesAIService assignResourcesAIService;

    @Autowired
    MoveCardsAIService moveCardsAIService;

    @Autowired
    DrawFromBacklogAIService drawFromBacklogAIService;

    private static final Logger logger = LoggerFactory.getLogger(PlayerService.class);

    public Game playTurn(Game game, Turn turn) {
        if (game != null && game.isValid() && turn != null) {
            adjustWipLimits(game, turn.getAdjustWipLimitsAction());
            if (game.getDifficultyLevel() == GameDifficulty.NORMAL) {
                game = playTurnNormal(game);
            }
        }
        return game;
    }

    private Game playTurnNormal(Game game) {
        game = assignResourcesWithAI(game);
        game = moveCardsWithAI(game);
        game = drawFromBacklogWithAI(game);
        return game;
    }

    private Game assignResourcesWithAI(Game game) {
        List<AssignResourcesAction> assignResourcesActions = assignResourcesAIService.getAssignResourcesActions(game);
        return assignResources(game, assignResourcesActions);
    }

    private Game moveCardsWithAI(Game game) {
        List<MoveCardAction> moveCardActions = moveCardsAIService.getMoveCardsActions(game);
        return moveCards(game, moveCardActions);
    }

    private Game drawFromBacklogWithAI(Game game) {
        List<DrawFromBacklogAction> drawFromBacklogActions = drawFromBacklogAIService.getDrawFromBacklogActions(game);
        return drawFromBacklog(game, drawFromBacklogActions);
    }

    public static Game adjustWipLimits(Game game, AdjustWipLimitsAction adjustWipLimitsAction) {
        if (game != null && game.isValid() && adjustWipLimitsAction != null) {
            game.getBoard().getPhases()
                    .forEach(phase -> {
                        String key = phase.getId();
                        Integer newWipLimitForPhaseOrUseOldIfNotGiven =
                                adjustWipLimitsAction.getPhaseWipLimits().getOrDefault(key, phase.getWipLimit());
                        phase.setWipLimit(newWipLimitForPhaseOrUseOldIfNotGiven);
                    });
        }
        return game;
    }

    public static Game assignResources(Game game, List<AssignResourcesAction> assignResourcesActions) {
        if (game != null && game.isValid() && assignResourcesActions != null) {
            assignResourcesActions.forEach(assignResourcesAction -> {
                try {
                    if (!isValidAssignResourcesAction(assignResourcesAction, game)) {
                        return;
                    }
                    game.performAssignResourcesAction(assignResourcesAction);
                }
                catch (CardPhasePointNotFoundException|PhaseNotFoundException|CardNotFoundException e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }
        return game;
    }

    public static Game moveCards(Game game, List<MoveCardAction> moveCardActions) {
        if (game != null && game.isValid() && moveCardActions != null) {
            moveCardActions.forEach(moveCardAction -> {
                try {
                    if (!isValidMoveCardAction(moveCardAction, game)) {
                        return;
                    }
                    game.performMoveCardAction(moveCardAction);
                }
                catch (ColumnNotFoundException|CardNotFoundException e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }
        return game;
    }

    public static Game drawFromBacklog(Game game, List<DrawFromBacklogAction> drawActions) {
        if (game != null && game.isValid() && drawActions != null) {
            Phase firstPhase = game.getBoard().getPhases().get(0);

            drawActions.forEach(drawAction -> {
                if (isValidDrawFromBacklogAction(firstPhase, drawAction)) {
                    List<Card> backlogDeck = game.getBoard().getBacklogDeck();
                    Card cardToDraw = backlogDeck.get(0);
                    Boolean cardSuccessfullyRemovedFromDeck = backlogDeck.remove(cardToDraw);
                    if (cardSuccessfullyRemovedFromDeck) {
                        Column columnWhereCardIsToBeSet = firstPhase.getColumns().get(0);
                        columnWhereCardIsToBeSet.getCards().add(drawAction.getIndexToPlaceCardAt(), cardToDraw);
                        cardToDraw.setDayStarted(game.getCurrentDay());
                    }
                }
            });
        }

        return game;
    }

    private static Boolean isValidDrawFromBacklogAction(Phase firstPhase, DrawFromBacklogAction drawAction) {
        return !firstPhase.isFullWip()
                && drawAction.getIndexToPlaceCardAt() >= 0
                && drawAction.getIndexToPlaceCardAt() <= firstPhase.getColumns().get(0).getCards().size();
    }

    private static boolean isValidAssignResourcesAction(AssignResourcesAction assignResourcesAction, Game game)
            throws CardNotFoundException, PhaseNotFoundException {
        Card card = game.getCardWithId(assignResourcesAction.getCardId());
        CardPhasePoint cardPhasePoint = card.getCardPhasePointOfPhase(assignResourcesAction.getPhaseId());
        Integer pointsLeft = cardPhasePoint.getTotalPoints() - cardPhasePoint.getPointsDone();
        return assignResourcesAction.getPoints() <= pointsLeft
                && game.isCardInFirstColumnOfPhase(card, assignResourcesAction.getPhaseId());
    }

    private static boolean isValidMoveCardAction(MoveCardAction moveCardAction, Game game)
            throws ColumnNotFoundException {
        return game.isColumnNextAdjacent(moveCardAction.getFromColumnId(), moveCardAction.getToColumnId()) &&
                !game.doesMoveExceedWIP(moveCardAction) &&
                game.isCardInColumn(moveCardAction.getCardId(), moveCardAction.getFromColumnId());
    }

}

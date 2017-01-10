package fi.aalto.ekanban.models.db.games;

import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE_ID;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.exceptions.*;
import fi.aalto.ekanban.models.*;
import fi.aalto.ekanban.models.db.phases.*;

@Document
public class Game {

    @Id
    private String id;
    @Field
    private String playerName;
    @Field
    private Board board;
    @Field
    private Integer currentDay;
    @Field
    private GameDifficulty difficultyLevel;
    @Field
    private Boolean hasEnded;
    @Field
    private Turn lastTurn;

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public Integer getCurrentDay() {
        return currentDay;
    }

    public void setCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
    }

    public GameDifficulty getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(GameDifficulty difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public Boolean getHasEnded() {
        return hasEnded;
    }

    public void setHasEnded(Boolean hasEnded) {
        this.hasEnded = hasEnded;
    }

    public Turn getLastTurn() {
        return lastTurn;
    }

    public void setLastTurn(Turn lastTurn) {
        this.lastTurn = lastTurn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Game)) return false;

        Game game = (Game) o;

        if (id != null ? !id.equals(game.id) : game.id != null) return false;
        if (playerName != null ? !playerName.equals(game.playerName) : game.playerName != null) return false;
        if (currentDay != null ? !currentDay.equals(game.currentDay) : game.currentDay != null) return false;
        if (difficultyLevel != null ? !difficultyLevel.equals(game.difficultyLevel) : game.difficultyLevel != null) return false;
        if (hasEnded != null ? !hasEnded.equals(game.hasEnded) : game.hasEnded != null) return false;
        if (lastTurn != null ? !lastTurn.equals(game.lastTurn) : game.lastTurn != null) return false;
        return board != null ? board.equals(game.board) : game.board == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        result = 31 * result + (board != null ? board.hashCode() : 0);
        result = 31 * result + (currentDay != null ? currentDay.hashCode() : 0);
        result = 31 * result + (difficultyLevel != null ? difficultyLevel.hashCode() : 0);
        result = 31 * result + (hasEnded != null ? hasEnded.hashCode() : 0);
        result = 31 * result + (lastTurn != null ? lastTurn.hashCode() : 0);
        return result;
    }

    @JsonIgnore
    public Boolean isValid() {
        return this.board != null && this.board.isValid();
    }

    public Column getColumnWithId(String columnId) {
        return this.board.getColumnWithId(columnId);
    }

    public Boolean isColumnNextAdjacent(String referenceColumnId, String inspectedOtherColumnId) {
        return this.board.isColumnNextAdjacent(referenceColumnId, inspectedOtherColumnId);
    }

    public void performMoveCardAction(MoveCardAction moveCardAction) {
        Column fromColumn = getColumnWithId(moveCardAction.getFromColumnId());
        Column toColumn = getColumnWithId(moveCardAction.getToColumnId());
        Card cardToMove = fromColumn.pullCard(moveCardAction.getCardId());
        toColumn.pushCard(cardToMove);
        if (isDeployedPhaseFirstColumn(toColumn)) {
            deployCard(cardToMove);
        }
    }

    public Boolean doesMoveExceedWIP(MoveCardAction moveCardAction) {
        return board.doesMoveExceedWIP(moveCardAction);
    }

    public Boolean isCardInColumn(String cardId, String columnId) {
        return board.isCardInColumn(cardId, columnId);
    }

    public Boolean isCardInFirstColumnOfPhase(Card card, String phaseId) {
        Phase phase = board.getPhaseWithId(phaseId);
        return phase.getColumns().get(0).getCards().contains(card);
    }

    public void performAssignResourcesAction(AssignResourcesAction assignResourcesAction) {
        Card card = getCardWithId(assignResourcesAction.getCardId());
        CardPhasePoint cardPhasePoint = card.getCardPhasePointOfPhase(assignResourcesAction.getPhaseId());
        cardPhasePoint.increasePointsDoneBy(assignResourcesAction.getPoints());
    }

    public Card getCardWithId(String cardId) {
        Optional<Card> cardOptional = board.getPhases().stream()
                .flatMap(phase -> phase.getColumns().stream())
                .flatMap(column -> column.getCards().stream())
                .filter(card -> card.getId().equals(cardId))
                .findFirst();
        if (!cardOptional.isPresent()) {
            throw new CardNotFoundException();
        }
        return cardOptional.get();
    }

    public Boolean hasNextPhase(Phase phase) {
        return board.hasNextPhase(phase);
    }

    public Phase getNextPhase(Phase phase) {
        return board.getNextPhase(phase);
    }

    public boolean canBeEnded() {
        return getBoard().backlogDeckEmpty() && getBoard().allCardsDeployed();
    }

    private void deployCard(Card cardDeployed) {
        cardDeployed.setDayDeployed(getCurrentDay());
        updateLeadTime(cardDeployed);
    }

    private void updateLeadTime(Card cardDeployed) {
        Integer leadTimeInDays = getCurrentDay() - cardDeployed.getDayStarted();
        cardDeployed.setLeadTimeInDays(leadTimeInDays);
    }

    private boolean isDeployedPhaseFirstColumn(Column column) {
        return getBoard().getPhaseWithId(DEPLOYED_PHASE_ID).getFirstColumn().equals(column);
    }
}

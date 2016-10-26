package fi.aalto.ekanban.models.db.games;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.exceptions.CardNotFoundException;
import fi.aalto.ekanban.exceptions.ColumnNotFoundException;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.models.db.phases.Column;

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
        return board != null ? board.equals(game.board) : game.board == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        result = 31 * result + (board != null ? board.hashCode() : 0);
        result = 31 * result + (currentDay != null ? currentDay.hashCode() : 0);
        return result;
    }

    public boolean isValid() {
        return this.board != null && this.board.isValid();
    }

    public Column getColumnWithId(String columnId) throws ColumnNotFoundException {
        return this.board.getColumnWithId(columnId);
    }

    public boolean isColumnNextAdjacent(String referenceColumnId, String inspectedOtherColumnId)
            throws ColumnNotFoundException {
        return this.board.isColumnNextAdjacent(referenceColumnId, inspectedOtherColumnId);
    }

    public void performMoveCardAction(MoveCardAction moveCardAction)
            throws ColumnNotFoundException, CardNotFoundException {
        board.performMoveCardAction(moveCardAction);
    }

    public boolean doesMoveExceedWIP(MoveCardAction moveCardAction) throws ColumnNotFoundException {
        return board.doesMoveExceedWIP(moveCardAction);
    }

    public boolean isCardInColumn(String cardId, String columnId) throws ColumnNotFoundException {
        return board.isCardInColumn(cardId, columnId);
    }
}

package fi.aalto.ekanban.models.games;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
public class Game {

    @Id
    private String id;
    @Field
    private String playerName;
    @Field
    private Board board;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game)) return false;

        Game game = (Game) o;

        if (id != null ? !id.equals(game.id) : game.id != null) return false;
        if (playerName != null ? !playerName.equals(game.playerName) : game.playerName != null) return false;
        return board != null ? board.equals(game.board) : game.board == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (playerName != null ? playerName.hashCode() : 0);
        result = 31 * result + (board != null ? board.hashCode() : 0);
        return result;
    }
}

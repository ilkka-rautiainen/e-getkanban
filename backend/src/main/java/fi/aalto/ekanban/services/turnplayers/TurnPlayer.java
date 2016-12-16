package fi.aalto.ekanban.services.turnplayers;

import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;

@FunctionalInterface
public interface TurnPlayer {
    public Game playTurn(Game game, Turn turn);
}

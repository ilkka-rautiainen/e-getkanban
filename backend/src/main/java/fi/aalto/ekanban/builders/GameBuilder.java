package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.db.games.Board;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.repositories.GameRepository;
import sun.nio.cs.ext.GB18030;

public final class GameBuilder {
    private String id;
    private String playerName;
    private Board board;
    private Integer currentDay;

    private GameBuilder() {
    }

    public static GameBuilder aGame() {
        return new GameBuilder();
    }

    public GameBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public GameBuilder withPlayerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public GameBuilder withBoard(Board board) {
        this.board = board;
        return this;
    }

    public GameBuilder withCurrentDay(Integer currentDay) {
        this.currentDay = currentDay;
        return this;
    }

    public Game create(GameRepository gameRepository) {
        Game game = new Game();
        game.setId(id);
        game.setPlayerName(playerName);
        game.setBoard(board);
        game.setCurrentDay(currentDay);
        return gameRepository.save(game);
    }

    public Game build() {
        Game game = new Game();
        game.setId(id);
        game.setPlayerName(playerName);
        game.setBoard(board);
        game.setCurrentDay(currentDay);
        return game;
    }
}

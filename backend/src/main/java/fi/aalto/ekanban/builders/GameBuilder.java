package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.Game;
import fi.aalto.ekanban.repositories.GameRepository;

public final class GameBuilder {
    private String id;
    private String playerName;

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

    public Game create(GameRepository gameRepository) {
        Game game = new Game();
        game.setId(id);
        game.setPlayerName(playerName);
        gameRepository.save(game);
        return game;
    }

    public Game build() {
        Game game = new Game();
        game.setId(id);
        game.setPlayerName(playerName);
        return game;
    }
}

package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.db.games.Board;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

public final class GameBuilder {
    private String id;
    private String playerName;
    private Board board;
    private Integer currentDay;

    private GameBuilder() {
        currentDay = 1;
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

    public GameBuilder withNormalDifficultyDefaults(String playerName, BaseCardRepository baseCardRepository,
                                                    PhaseRepository phaseRepository) {
        this.playerName = playerName;
        this.board = BoardBuilder.aBoard()
                .withNormalDifficultyDefaults(baseCardRepository, phaseRepository)
                .build();
        return this;
    }

    public GameBuilder withNormalDifficultyMockDefaults(String playerName) {
        this.playerName = playerName;
        this.board = BoardBuilder.aBoard()
                .withNormalDifficultyMockDefaults()
                .build();
        return this;
    }

    public Game create(GameRepository gameRepository) {
        Game game = build();
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

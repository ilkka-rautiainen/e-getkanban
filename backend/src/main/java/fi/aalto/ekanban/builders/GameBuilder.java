package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.CFD;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.gameconfigurations.DifficultyConfiguration;
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
    private GameDifficulty difficultyLevel;
    private Boolean hasEnded;
    private Turn lastTurn;
    private CFD cfd;

    private GameBuilder() {
        currentDay = 0;
        hasEnded = false;
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

    public GameBuilder withDifficultyLevel(GameDifficulty difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
        return this;
    }

    public GameBuilder withHasEnded(Boolean hasEnded) {
        this.hasEnded = hasEnded;
        return this;
    }

    public GameBuilder withLastTurn(Turn lastTurn) {
        this.lastTurn = lastTurn;
        return this;
    }

    public GameBuilder withCFD(CFD cfd) {
        this.cfd = cfd;
        return this;
    }

    public GameBuilder withNormalDifficultyDefaults(DifficultyConfiguration difficultyConfiguration,
                                                    String playerName,
                                                    BaseCardRepository baseCardRepository,
                                                    PhaseRepository phaseRepository) {
        this.playerName = playerName;
        this.difficultyLevel = GameDifficulty.NORMAL;
        this.board = BoardBuilder.aBoard()
                .withNormalDifficultyDefaults(difficultyConfiguration, baseCardRepository, phaseRepository)
                .build();
        this.cfd = CFDBuilder.aCFD()
                .withCfdDailyValues(CFDDailyValuesBuilder.aSetOfCfdDailyValues()
                        .withOneDailyValueWithZerosBasedOnPhases(this.board.getPhases())
                        .build())
                .build();
        return this;
    }

    public GameBuilder withNormalDifficultyMockDefaults(String playerName) {
        this.playerName = playerName;
        this.difficultyLevel = GameDifficulty.NORMAL;
        this.board = BoardBuilder.aBoard()
                .withNormalDifficultyMockDefaults()
                .build();
        this.cfd = CFDBuilder.aCFD()
                .withCfdDailyValues(CFDDailyValuesBuilder.aSetOfCfdDailyValues()
                        .withOneDailyValueWithZerosBasedOnPhases(this.board.getPhases())
                        .build())
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
        game.setDifficultyLevel(difficultyLevel);
        game.setHasEnded(hasEnded);
        game.setLastTurn(lastTurn);
        game.setCFD(cfd);
        return game;
    }
}

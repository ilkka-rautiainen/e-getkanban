package fi.aalto.ekanban.services;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.*;
import fi.aalto.ekanban.repositories.GameRepository;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private GameInitService gameInitService;
    private PlayerService playerService;
    private GameOptionService gameOptionService;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    public GameService(GameInitService gameInitService,
                       PlayerService playerService,
                       GameOptionService gameOptionService) {
        this.gameInitService = gameInitService;
        this.playerService = playerService;
        this.gameOptionService = gameOptionService;
    }

    @Transactional
    public Game startGame(String playerName, GameDifficulty gameDifficulty) {
        Game newGameWithDifficultyLevelSpecificInitializedOptions = gameInitService.getInitializedGame(gameDifficulty);
        newGameWithDifficultyLevelSpecificInitializedOptions.setPlayerName(playerName);

        List<Card> backlogDeck = Arrays.asList(CardBuilder.aCard().build());
        List<EventCard> eventCardDeck = Arrays.asList(EventCardBuilder.anEventCard().build());
        List<Phase> phases = Arrays.asList(
                PhaseBuilder.aPhase().analysis().build(),
                PhaseBuilder.aPhase().development().build(),
                PhaseBuilder.aPhase().test().build(),
                PhaseBuilder.aPhase().deployed().build()
        );

        Board gameBoard = BoardBuilder.aBoard()
            .withBacklogDeck(backlogDeck)
            .withEventCardDeck(eventCardDeck)
            .withPhases(phases)
            .build();

        newGameWithDifficultyLevelSpecificInitializedOptions.setBoard(gameBoard);
        Game createdGame = gameRepository.save(newGameWithDifficultyLevelSpecificInitializedOptions);
        return createdGame;
    }

    public Game playTurn(String gameId, Turn turn) {
        return null;
    }

}

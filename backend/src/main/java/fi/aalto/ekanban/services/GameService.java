package fi.aalto.ekanban.services;

import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT;
import static fi.aalto.ekanban.ApplicationConstants.TEST;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED;

import java.util.ArrayList;
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
import fi.aalto.ekanban.models.db.gameconfigurations.BaseCard;
import fi.aalto.ekanban.models.db.games.*;
import fi.aalto.ekanban.models.db.gameconfigurations.Phase;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;

@Service
public class GameService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameService.class);

    private GameInitService gameInitService;
    private PlayerService playerService;
    private GameOptionService gameOptionService;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PhaseRepository phaseRepository;
    @Autowired
    private BaseCardRepository baseCardRepository;

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
        Game newGameWithDifficultyLevelOptions = gameInitService.getInitializedGame(gameDifficulty);
        newGameWithDifficultyLevelOptions.setPlayerName(playerName);

        Board gameBoard = BoardBuilder.aBoard()
            .withBacklogDeck(initNormalCards())
            .withPhases(initNormalPhases())
            .build();

        newGameWithDifficultyLevelOptions.setBoard(gameBoard);
        Game createdGame = gameRepository.save(newGameWithDifficultyLevelOptions);
        return createdGame;
    }

    public Game playTurn(String gameId, Turn turn) {
        return null;
    }

    private List<Phase> initNormalPhases() {
        Phase analysis = phaseRepository.findByName(ANALYSIS);
        Phase development = phaseRepository.findByName(DEVELOPMENT);
        Phase test = phaseRepository.findByName(TEST);
        Phase deployment = phaseRepository.findByName(DEPLOYED);

        return Arrays.asList(analysis, development, test, deployment);
    }

    private List<Card> initNormalCards() {
        List<Card> backlogDeck = new ArrayList<>();
        List<BaseCard> baseCards = baseCardRepository.findAll();

        for (Integer index = 0; index < baseCards.size(); index++) {
            BaseCard baseCard = baseCards.get(index);
            Card cardForGameCreatedFromBaseCard = CardBuilder.aCard()
                    .fromBaseCard(baseCard)
                    .withOrderNumber(index+1)
                    .build();
            backlogDeck.add(cardForGameCreatedFromBaseCard);
        }
        return backlogDeck;
    }

}

package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT;
import static fi.aalto.ekanban.ApplicationConstants.TEST;
import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED;

import java.util.Arrays;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fi.aalto.ekanban.builders.BaseCardBuilder;
import fi.aalto.ekanban.builders.PhaseBuilder;
import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.repositories.BaseCardRepository;
import fi.aalto.ekanban.repositories.PhaseRepository;
import fi.aalto.ekanban.repositories.GameRepository;

@RunWith(HierarchicalContextRunner.class)
public class GameServiceTest {

    @InjectMocks
    private static GameService gameService;

    private static GameInitService gameInitService;
    private static PlayerService playerService;
    private static GameOptionService gameOptionService;

    @Mock
    private static GameRepository gameRepository;
    @Mock
    private static PhaseRepository phaseRepository;
    @Mock
    private static BaseCardRepository baseCardRepository;

    @BeforeClass
    public static void initService() {
        gameInitService = Mockito.mock(GameInitService.class);
        playerService = Mockito.mock(PlayerService.class);
        gameOptionService = Mockito.mock(GameOptionService.class);
        gameService = new GameService(gameInitService, playerService, gameOptionService);
    }

    @Before
    public void initGameRepositoryMock() {
        MockitoAnnotations.initMocks(this);
    }

    public class startGame {

        GameDifficulty gameDifficulty;
        Game newGame;

        @Before
        public void setupContextAndCallMethod() {
            Game blankGame = GameBuilder.aGame().build();
            String playerName = "Player";
            gameDifficulty = GameDifficulty.NORMAL;

            Mockito.when(gameInitService.getInitializedGame(gameDifficulty)).thenReturn(blankGame);
            Mockito.when(gameRepository.save(Mockito.any(Game.class))).thenReturn(blankGame);
            Mockito.when(phaseRepository.findByName(ANALYSIS)).thenReturn(PhaseBuilder.aPhase().analysis().build());
            Mockito.when(phaseRepository.findByName(DEVELOPMENT)).thenReturn(PhaseBuilder.aPhase().development().build());
            Mockito.when(phaseRepository.findByName(TEST)).thenReturn(PhaseBuilder.aPhase().test().build());
            Mockito.when(phaseRepository.findByName(DEPLOYED)).thenReturn(PhaseBuilder.aPhase().deployed().build());
            Mockito.when(baseCardRepository.findAll()).thenReturn(Arrays.asList(BaseCardBuilder.aBaseCard().build()));

            newGame = gameService.startGame(playerName, gameDifficulty);
        }

        @Test
        public void shouldReturnNewGame() { assertThat(newGame, is(notNullValue())); }

        @Test
        public void shouldCallOtherServices() {
            Mockito.verify(gameInitService, Mockito.times(1)).getInitializedGame(gameDifficulty);
            Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any(Game.class));
            Mockito.verify(phaseRepository, Mockito.times(4)).findByName(Mockito.anyString());
            Mockito.verify(baseCardRepository, Mockito.times(1)).findAll();
        }

    }

}

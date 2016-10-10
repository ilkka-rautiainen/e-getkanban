package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.games.Game;

@RunWith(HierarchicalContextRunner.class)
public class GameServiceTest {

    @InjectMocks
    private static GameService gameService;

    private static GameInitService gameInitService;
    private static PlayerService playerService;
    private static GameOptionService gameOptionService;

    @Mock
    private static GameRepository gameRepository;

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

            newGame = gameService.startGame(playerName, gameDifficulty);
        }

        @Test
        public void shouldReturnNewGame() {assertThat(newGame, is(notNullValue())); }

        @Test
        public void shouldCallOtherServices() {
            Mockito.verify(gameInitService, Mockito.times(1)).getInitializedGame(gameDifficulty);
            Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any(Game.class));
        }

    }

}

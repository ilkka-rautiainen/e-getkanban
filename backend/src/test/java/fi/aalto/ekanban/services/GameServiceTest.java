package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.exceptions.GameHasEndedException;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.utils.TestGameContainer;
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
        String playerName;

        @Before
        public void setupContextAndCallMethod() {
            Game blankGame = GameBuilder.aGame().build();
            playerName = "Player";
            gameDifficulty = GameDifficulty.NORMAL;

            Mockito.when(gameInitService.getInitializedGame(gameDifficulty, playerName)).thenReturn(blankGame);
            Mockito.when(gameRepository.save(Mockito.any(Game.class))).thenReturn(blankGame);

            newGame = gameService.startGame(playerName, gameDifficulty);
        }

        @Test
        public void shouldReturnNewGame() { assertThat(newGame, is(notNullValue())); }

        @Test
        public void shouldCallOtherServices() {
            Mockito.verify(gameInitService, Mockito.times(1)).getInitializedGame(gameDifficulty, playerName);
            Mockito.verify(gameRepository, Mockito.times(1)).save(Mockito.any(Game.class));
        }

    }

    public class playTurn {
        private Turn turn;
        private Game game;

        private void initMocks() {
            Mockito.when(gameRepository.findOne(game.getId())).thenReturn(game);
            Mockito.when(playerService.playTurn(Mockito.any(Game.class), Mockito.any(Turn.class))).then(returnsFirstArg());
            Mockito.when(gameRepository.save(game)).thenReturn(game);
        }

        public class withOngoingGame {
            private Game newGame;

            @Before
            public void initAndDoAction() {
                initGame();
                initMocks();

                turn = TurnBuilder.aTurn().build();
                newGame = gameService.playTurn(game.getId(), turn);
            }

            private void initGame() {
                TestGameContainer initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
                initialGameContainer.fillFirstWorkPhasesWithSomeReadyCards();
                initialGameContainer.fillLastWorkPhaseWithSomeReadyCards();
                game = initialGameContainer.getGame();
            }

            @Test
            public void shouldReturnPlayedGame() {
                assertThat(newGame, is(notNullValue()));
            }

            @Test
            public void shouldCallOtherServices() {
                Mockito.verify(gameRepository, Mockito.times(1)).findOne(game.getId());
                Mockito.verify(playerService, Mockito.times(1)).playTurn(game, turn);
                Mockito.verify(gameRepository, Mockito.times(1)).save(game);
            }
        }

        public class withEndedGame {
            @Before
            public void init() {
                initGame();
                initMocks();
                turn = TurnBuilder.aTurn().build();
            }

            private void initGame() {
                TestGameContainer initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
                initialGameContainer.emptyBacklog();
                initialGameContainer.getGame().setHasEnded(true);
                game = initialGameContainer.getGame();
            }

            @Test(expected=GameHasEndedException.class)
            public void shouldThrowAnError() {
                gameService.playTurn(game.getId(), turn);
            }
        }
    }
}

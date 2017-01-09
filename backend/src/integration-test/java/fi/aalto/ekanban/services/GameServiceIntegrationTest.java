package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.List;
import java.util.stream.Collectors;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.TurnBuilder;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.Turn;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.models.CFDDailyValue;
import fi.aalto.ekanban.repositories.GameRepository;
import fi.aalto.ekanban.utils.TestGameContainer;
import fi.aalto.ekanban.SpringIntegrationTest;

@RunWith(HierarchicalContextRunner.class)
public class GameServiceIntegrationTest extends SpringIntegrationTest {

    @Autowired
    private GameService gameService;

    @Autowired
    private GameRepository gameRepository;

    public class startGame {

        private Game newGame;
        private String playerName;
        private GameDifficulty gameDifficulty;

        public class whenDifficultyIsNormal {

            private Long gameCountBeforeAction;
            private List<CFDDailyValue> cfdDailyValues;

            @Before
            public void setupGame() {
                gameCountBeforeAction = gameRepository.count();
                gameDifficulty = GameDifficulty.NORMAL;
                playerName = "Player";
                newGame = gameService.startGame(playerName, gameDifficulty);

                cfdDailyValues = newGame.getCFD().getCfdDailyValues();
            }

            @Test
            public void shouldReturnCreatedGame() {
                assertThat(gameRepository.count(), equalTo(gameCountBeforeAction + 1));
                assertThat(newGame, is(notNullValue()));
            }

            @Test
            public void boardShouldHaveBoardEnteredTracklineColor() {
                assertThat(newGame.getBoard().getEnteredBoardTrackLineColor(), is(notNullValue()));
            }

            @Test
            public void gameShouldHaveCFD() {
                assertThat(newGame.getCFD(), is(notNullValue()));
            }

            @Test
            public void cfdDailyValuesShouldHaveOneItem() {
                assertThat(cfdDailyValues.size(), equalTo(1));
            }

            @Test
            public void firstCfdDailyValueShouldHaveZeroValues() {
                CFDDailyValue firstDailyValue = cfdDailyValues.get(0);
                assertThat(firstDailyValue.getEnteredBoard(), equalTo(0));
                firstDailyValue.getPhaseValues().values().forEach(dailyValue -> assertThat(dailyValue, equalTo(0)));
            }

            @Test
            public void cfdDailyValueShouldIncludeAllPhasesWithATrackLine() {
                CFDDailyValue firstDailyValue = cfdDailyValues.get(0);
                List<Phase> phasesWithTrackLine = newGame.getBoard().getPhases().stream()
                        .filter(phase -> phase.getTrackLinePlace() != null)
                        .collect(Collectors.toList());
                phasesWithTrackLine.forEach(phase -> {
                    assertThat(firstDailyValue.getPhaseValues(), hasKey(phase.getId()));
                });
                assertThat(firstDailyValue.getPhaseValues().size(), equalTo(phasesWithTrackLine.size()));
            }

        }

    }

    public class playTurn {
        Game newGame;

        @Before
        public void setup() {
            TestGameContainer initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
            initialGameContainer.fillFirstWorkPhasesWithSomeAlmostReadyCards();
            initialGameContainer.fillLastWorkPhaseWithSomeAlmostReadyCards();
            Game initialGame = gameRepository.save(initialGameContainer.getGame());
            Turn blankTurn = TurnBuilder.aTurn().build();
            newGame = gameService.playTurn(initialGame.getId(), blankTurn);
        }

        @Test
        public void gameShouldHaveLastTurn() {
            assertThat(newGame.getLastTurn(), is(notNullValue()));
        }

        @Test
        public void gameLastTurnShouldHaveAssignResourceActions() {
            assertThat(newGame.getLastTurn().getAssignResourcesActions(), is(not(empty())));
        }

        @Test
        public void gameLastTurnShouldHaveMoveCardsActions() {
            assertThat(newGame.getLastTurn().getMoveCardActions(), is(not(empty())));
        }

        @Test
        public void gameLastTurnShouldHaveDrawFromBacklogActions() {
            assertThat(newGame.getLastTurn().getDrawFromBacklogActions(), is(not(empty())));
        }

    }

}

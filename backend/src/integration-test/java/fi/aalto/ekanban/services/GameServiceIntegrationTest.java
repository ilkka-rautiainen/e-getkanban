package fi.aalto.ekanban.services;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static fi.aalto.ekanban.ApplicationConstants.ANALYSIS_PHASE_ID;
import static fi.aalto.ekanban.ApplicationConstants.DEVELOPMENT_PHASE_ID;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.rits.cloning.Cloner;
import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.AssignResourcesAction;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.phases.Column;
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
        private Long gameCountBeforeAction;

        public class whenDifficultyIsNormal {

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

        public class whenDifficultyIsAdvanced {

            @Before
            public void setupGame() {
                gameCountBeforeAction = gameRepository.count();
                gameDifficulty = GameDifficulty.ADVANCED;
                playerName = "Player";
                newGame = gameService.startGame(playerName, gameDifficulty);
            }

            @Test
            public void shouldReturnCreatedGame() {
                assertThat(gameRepository.count(), equalTo(gameCountBeforeAction + 1));
                assertThat(newGame, is(notNullValue()));
            }

            @Test
            public void shouldReturnGameWithAdvancedDifficulty() {
                assertThat(newGame.getDifficultyLevel(), equalTo(GameDifficulty.ADVANCED));
            }

        }

    }

    public class playTurn {
        Game newGame;

        public class withNormalDifficulty {
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

        public class withMediumDifficulty {

            private List<CardPhasePoint> analysisCardPointsBefore;
            private List<CardPhasePoint> developmentSecondCardPointsBefore;

            private Card cardInAnalysis;
            private Card secondCardInDevelopment;

            @Before
            public void setup() {
                Cloner cloner = new Cloner();
                TestGameContainer initialGameContainer = TestGameContainer.withNormalDifficultyMockGame();
                Game game = initialGameContainer.getGame();
                game.setDifficultyLevel(GameDifficulty.MEDIUM);

                Phase analysisPhase = game.getBoard().getWorkPhases().get(0);
                Phase developmentPhase = game.getBoard().getWorkPhases().get(1);

                Column analysisInProgressColumn = analysisPhase.getFirstColumn();
                Column developmentInProgressColumn = developmentPhase.getFirstColumn();

                cardInAnalysis = CardBuilder.aCard().withMockPhasePoints().build();
                analysisCardPointsBefore = cloner.deepClone(cardInAnalysis.getCardPhasePoints());
                Card cardInDevelopment = CardBuilder.aCard().withMockPhasePoints().build();
                secondCardInDevelopment = CardBuilder.aCard().withMockPhasePoints().build();
                developmentSecondCardPointsBefore = cloner.deepClone(secondCardInDevelopment.getCardPhasePoints());

                analysisInProgressColumn.setCards(Arrays.asList(cardInAnalysis));
                developmentInProgressColumn.setCards(Arrays.asList(cardInDevelopment, secondCardInDevelopment));

                AssignResourcesAction assignResourcesActionWithDevelopmentDieToAnalysisCard =
                        AssignResourcesActionBuilder.anAssignResourcesAction()
                                .withCardId(cardInAnalysis.getId())
                                .withCardPhaseId(analysisPhase.getId())
                                .withDieIndex(1)
                                .withDiePhaseId(developmentPhase.getId())
                                .build();

                AssignResourcesAction assignResourcesActionWithAnalysisDieToDevelopmentCard =
                        AssignResourcesActionBuilder.anAssignResourcesAction()
                                .withCardId(secondCardInDevelopment.getId())
                                .withCardPhaseId(developmentPhase.getId())
                                .withDieIndex(0)
                                .withDiePhaseId(analysisPhase.getId())
                                .build();

                List<AssignResourcesAction> assignResourcesActions = Arrays.asList(
                        assignResourcesActionWithAnalysisDieToDevelopmentCard,
                        assignResourcesActionWithDevelopmentDieToAnalysisCard
                );

                Turn turnWithAssignResources = TurnBuilder.aTurn()
                        .withAssignResourcesActions(assignResourcesActions)
                        .build();

                Game initialGame = gameRepository.save(game);
                newGame = gameService.playTurn(initialGame.getId(), turnWithAssignResources);
                cardInAnalysis = newGame.getBoard().getPhases().get(0).getColumns().get(0).getCards().get(0);
                secondCardInDevelopment = newGame.getBoard().getPhases().get(1).getColumns().get(0).getCards().get(1);
            }

            @Test
            public void shouldAssignResourcesToSelectedCards() {
                Integer firstCardAnalysisPointsDoneBefore = analysisCardPointsBefore.get(0).getPointsDone();
                Integer secondCardDevelopmentPointsDoneBefore = developmentSecondCardPointsBefore.get(1).getPointsDone();
                assertThat(firstCardAnalysisPointsDoneBefore,
                        lessThan(cardInAnalysis.getCardPhasePointOfPhase(ANALYSIS_PHASE_ID).getPointsDone()));
                assertThat(secondCardDevelopmentPointsDoneBefore,
                        lessThan(secondCardInDevelopment.getCardPhasePointOfPhase(DEVELOPMENT_PHASE_ID).getPointsDone()));
            }

        }

    }

}

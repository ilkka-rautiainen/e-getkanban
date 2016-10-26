package fi.aalto.ekanban.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.enums.FinancialValue;
import fi.aalto.ekanban.models.db.games.Board;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

public class TestGameContainer {
    private static final Integer FULL_WIP = 0;
    private static final Integer ONE_TO_FULL_WIP = 1;
    private Game game;

    public static TestGameContainer withNormalDifficultyGame() {
        TestGameContainer testGameContainer = new TestGameContainer();
        testGameContainer.initializeWithNormalDifficultyGame();
        return testGameContainer;
    }

    public static TestGameContainer withGame(Game game) {
        return new TestGameContainer(game);
    }

    public Game getGame() {
        return game;
    }

    public enum ColumnName {
        ANALYSIS_IN_PROGRESS,
        ANALYSIS_DONE,
        DEVELOPMENT_IN_PROGRESS,
        DEVELOPMENT_DONE,
        TEST
    }

    public Column getColumn(ColumnName column) {
        switch (column) {
            case ANALYSIS_IN_PROGRESS:
                return getAnalysisPhase().getColumns().get(0);
            case ANALYSIS_DONE:
                return getAnalysisPhase().getColumns().get(1);
            case DEVELOPMENT_IN_PROGRESS:
                return getDevelopmentPhase().getColumns().get(0);
            case DEVELOPMENT_DONE:
                return getDevelopmentPhase().getColumns().get(1);
            case TEST:
                return getTestPhase().getColumns().get(0);
            default:
                return null;
        }
    }

    public void fillDevelopmentInProgressToOneToFullWip() {
        fillDevelopmentInProgressTowardsFullWip(ONE_TO_FULL_WIP);
    }

    public void fillDevelopmentInProgressToFullWip() {
        fillDevelopmentInProgressTowardsFullWip(FULL_WIP);
    }

    private void fillDevelopmentInProgressTowardsFullWip(Integer amountToLeaveUnfilled) {
        Phase development = getDevelopmentPhase();
        Integer cardsToAddUntilFullEnough = development.getWipLimit()
                - development.getTotalAmountOfCards()
                - amountToLeaveUnfilled;

        Column developmentInProgressColumn = getColumn(ColumnName.DEVELOPMENT_IN_PROGRESS);
        for (int i=0; i<cardsToAddUntilFullEnough; i++) {
            developmentInProgressColumn.getCards().add(CardBuilder.aCard().build());
        }
    }

    public Phase getAnalysisPhase() {
        return game.getBoard().getPhases().get(0);
    }

    public Phase getDevelopmentPhase() {
        return getGame().getBoard().getPhases().get(1);
    }

    private TestGameContainer() { }

    private TestGameContainer(Game game) {
        this.game = game;
    }


    private Phase getTestPhase() {
        return game.getBoard().getPhases().get(2);
    }

    private void initializeWithNormalDifficultyGame() {
        Phase analysisPhase = PhaseBuilder.aPhase().analysis().withId("1").build();
        Phase developmentPhase = PhaseBuilder.aPhase().development().withId("2").build();
        Phase testPhase = PhaseBuilder.aPhase().test().withId("3").build();
        Phase deployedPhase = PhaseBuilder.aPhase().deployed().withId("4").build();

        List<Phase> normalDifficultyPhases = Arrays.asList(analysisPhase, developmentPhase, testPhase, deployedPhase);
        List<Card> backlogDeck = buildBacklogDeck(analysisPhase, developmentPhase, testPhase);

        Board gameBoard = BoardBuilder.aBoard()
                .withBacklogDeck(backlogDeck)
                .withPhases(normalDifficultyPhases)
                .build();

        game = GameBuilder.aGame()
                .withBoard(gameBoard)
                .withPlayerName("player")
                .build();
    }

    private static List<Card> buildBacklogDeck(Phase analysisPhase, Phase developmentPhase, Phase testPhase) {
        CardPhasePoint analysisCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                .withTotalPoints(5)
                .withPhaseId(analysisPhase.getId())
                .build();
        CardPhasePoint developmentCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                .withTotalPoints(5)
                .withPhaseId(developmentPhase.getId())
                .build();
        CardPhasePoint testCardPoints = CardPhasePointBuilder.aCardPhasePoint()
                .withTotalPoints(5)
                .withPhaseId(testPhase.getId())
                .build();
        List<CardPhasePoint> cardPhasePoints = Arrays.asList(analysisCardPoints, developmentCardPoints, testCardPoints);

        List<Card> backlogDeck = new ArrayList<>();

        for (Integer i = 0; i < 15; i=i+3) {
            backlogDeck.add(CardBuilder.aCard()
                    .withFinancialValue(FinancialValue.HIGH)
                    .withCardPhasePoints(cardPhasePoints)
                    .withSubscribesWhenDeployed("1")
                    .withOrderNumber(i+1)
                    .build());
            backlogDeck.add(CardBuilder.aCard()
                    .withFinancialValue(FinancialValue.MED)
                    .withCardPhasePoints(cardPhasePoints)
                    .withSubscribesWhenDeployed("1")
                    .withOrderNumber(i+2)
                    .build());
            backlogDeck.add(CardBuilder.aCard()
                    .withFinancialValue(FinancialValue.LOW)
                    .withCardPhasePoints(cardPhasePoints)
                    .withSubscribesWhenDeployed("1")
                    .withOrderNumber(i+3)
                    .build());
        }

        return backlogDeck;
    }

}

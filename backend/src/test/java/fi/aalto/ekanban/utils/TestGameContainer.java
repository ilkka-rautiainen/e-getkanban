package fi.aalto.ekanban.utils;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

public class TestGameContainer {
    private static final Integer FULL_WIP = 0;
    private static final Integer ONE_TO_FULL_WIP = 1;
    private Game game;

    private TestGameContainer() { }

    private TestGameContainer(Game game) {
        this.game = game;
    }

    public static TestGameContainer withNormalDifficultyMockGame() {
        TestGameContainer testGameContainer = new TestGameContainer();
        testGameContainer.initializeWithNormalDifficultyMockGame();
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

    public void addCardsWithMockPhasePointsToDevelopmentInProgress(Integer cardsToAdd) {
        Column developmentInProgressColumn = getColumn(ColumnName.DEVELOPMENT_IN_PROGRESS);
        for (Integer i = 0; i < cardsToAdd; i++) {
            developmentInProgressColumn.getCards().add(CardBuilder.aCard().withMockPhasePoints().build());
        }
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

    private Phase getTestPhase() {
        return game.getBoard().getPhases().get(2);
    }

    private void initializeWithNormalDifficultyMockGame() {
        String playerName = "player";
        game = GameBuilder.aGame()
                .withNormalDifficultyMockDefaults(playerName)
                .build();
    }

}

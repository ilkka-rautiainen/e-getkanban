package fi.aalto.ekanban.utils;

import java.util.Arrays;

import fi.aalto.ekanban.builders.*;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

import java.util.List;

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
        TEST,
        DEPLOYED
    }

    public Column getColumn(ColumnName column) {
        switch (column) {
            case ANALYSIS_IN_PROGRESS:
                return getAnalysisPhase().getFirstColumn();
            case ANALYSIS_DONE:
                return getAnalysisPhase().getSecondColumn();
            case DEVELOPMENT_IN_PROGRESS:
                return getDevelopmentPhase().getFirstColumn();
            case DEVELOPMENT_DONE:
                return getDevelopmentPhase().getSecondColumn();
            case TEST:
                return getTestPhase().getFirstColumn();
            case DEPLOYED:
                return getDeployedPhase().getFirstColumn();
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

    public void fillFirstWorkPhasesWithSomeCardsInProgress() {
        List<Phase> firstWorkPhases = getFirstWorkPhases();
        PhasePopulater.fillWithCards(firstWorkPhases, PhasePopulater.FillingType.IN_PROGRESS_SOME);
    }

    public void fillFirstWorkPhasesToFullWithReadyCards() {
        List<Phase> firstWorkPhases = getFirstWorkPhases();
        PhasePopulater.fillWithCards(firstWorkPhases, PhasePopulater.FillingType.READY_FULL);
    }

    public void fillFirstWorkPhasesWithSomeReadyCards() {
        List<Phase> firstWorkPhases = getFirstWorkPhases();
        PhasePopulater.fillWithCards(firstWorkPhases, PhasePopulater.FillingType.READY_SOME);
    }

    public void fillFirstWorkPhasesWithSomeAlmostReadyCards() {
        List<Phase> firstWorkPhases = getFirstWorkPhases();
        PhasePopulater.fillWithCards(firstWorkPhases, PhasePopulater.FillingType.ALMOST_READY_SOME);
    }

    public void fillFirstWorkPhasesToFullWithAlmostReadyCards() {
        List<Phase> firstWorkPhases = getFirstWorkPhases();
        PhasePopulater.fillWithCards(firstWorkPhases, PhasePopulater.FillingType.ALMOST_READY_FULL);
    }

    public void fillFirstWorkPhasesToFullWithCardsInProgress() {
        List<Phase> firstWorkPhases = getFirstWorkPhases();
        PhasePopulater.fillWithCards(firstWorkPhases, PhasePopulater.FillingType.IN_PROGRESS_FULL);
    }

    public void fillDevelopmentWithSomeReadyCards() {
        Phase developmentPhase = getDevelopmentPhase();
        PhasePopulater.fillWithCards(developmentPhase, PhasePopulater.FillingType.READY_SOME);
    }

    public void fillDevelopmentToFullWithReadyCards() {
        Phase developmentPhase = getDevelopmentPhase();
        PhasePopulater.fillWithCards(developmentPhase, PhasePopulater.FillingType.READY_FULL);
    }

    public void fillLastWorkPhaseWithSomeCardsInProgress() {
        Phase lastWorkPhase = getTestPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.IN_PROGRESS_SOME);
    }

    public void fillTestingToFullWithCardsInProgress() {
        fillLastWorkPhaseToFullWithCardsInProgress();
    }

    public void fillLastWorkPhaseToFullWithCardsInProgress() {
        Phase lastWorkPhase = getTestPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.IN_PROGRESS_FULL);
    }

    public void fillLastWorkPhaseToFullWithReadyCards() {
        Phase lastWorkPhase = getTestPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.READY_FULL);
    }

    public void fillTestingWithSomeReadyCards() {
        fillLastWorkPhaseWithSomeReadyCards();
    }

    public void fillLastWorkPhaseWithSomeReadyCards() {
        Phase lastWorkPhase = getTestPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.READY_SOME);
    }

    public void fillLastWorkPhaseToAlmostFullWithCardsInProgress() {
        Phase lastWorkPhase = getTestPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.IN_PROGRESS_ALMOST_FULL);
    }

    public void fillLastWorkPhaseWithSomeAlmostReadyCards() {
        Phase lastWorkPhase = getTestPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.ALMOST_READY_SOME);
    }

    public void fillLastWorkPhaseToFullWithAlmostReadyCards() {
        Phase lastWorkPhase = getTestPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.ALMOST_READY_FULL);
    }

    public void removeAllButOneCardFromBacklog() {
        List<Card> backlogDeck = game.getBoard().getBacklogDeck();
        backlogDeck.removeAll(backlogDeck.subList(1, backlogDeck.size()));
    }

    public void emptyBacklog() {
        game.getBoard().getBacklogDeck().clear();
    }

    public void addCardsWithMockPhasePointsToColumn(Integer cardsToAdd, ColumnName columnName) {
        Column column = getColumn(columnName);
        for (Integer i = 0; i < cardsToAdd; i++) {
            column.getCards().add(CardBuilder.aCard().withMockPhasePoints().build());
        }
    }

    public void fillCFDWithZeroValuesUntilDay(Integer day) {
        getGame().setCFD(CFDBuilder.aCFD()
                .withCfdDailyValues(CFDDailyValuesBuilder.aSetOfCfdDailyValues()
                        .withValuesWithZerosBasedOnPhasesUntilDay(getGame().getBoard().getPhases(), day)
                        .build())
                .build());
    }

    public Phase getAnalysisPhase() {
        return game.getBoard().getPhases().get(0);
    }

    public Phase getDevelopmentPhase() {
        return getGame().getBoard().getPhases().get(1);
    }

    public Phase getTestPhase() {
        return game.getBoard().getPhases().get(2);
    }

    public Phase getDeployedPhase() {
        return game.getBoard().getPhases().get(3);
    }

    private void initializeWithNormalDifficultyMockGame() {
        String playerName = "player";
        game = GameBuilder.aGame()
                .withNormalDifficultyMockDefaults(playerName)
                .build();
    }

    private void fillDevelopmentInProgressTowardsFullWip(Integer amountToLeaveUnfilled) {
        Phase development = getDevelopmentPhase();
        Integer cardsToAddUntilFullEnough = development.getWipLimit()
                - development.getTotalAmountOfCards()
                - amountToLeaveUnfilled;

        addCardsWithMockPhasePointsToColumn(cardsToAddUntilFullEnough, ColumnName.DEVELOPMENT_IN_PROGRESS);
    }

    private List<Phase> getFirstWorkPhases() {
        return Arrays.asList(getAnalysisPhase(), getDevelopmentPhase());
    }

}

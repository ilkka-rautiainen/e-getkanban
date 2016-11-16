package fi.aalto.ekanban.utils;

import java.util.stream.Collectors;

import fi.aalto.ekanban.builders.CardBuilder;
import fi.aalto.ekanban.builders.GameBuilder;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

import java.util.Collections;
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

    public void fillLastWorkPhaseWithSomeCardsInProgress() {
        Phase lastWorkPhase = getLastWorkPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.IN_PROGRESS_SOME);
    }

    public void fillLastWorkPhaseToFullWithCardsInProgress() {
        Phase lastWorkPhase = getLastWorkPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.IN_PROGRESS_FULL);
    }

    public void fillLastWorkPhaseToFullWithReadyCards() {
        Phase lastWorkPhase = getLastWorkPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.READY_FULL);
    }

    public void fillLastWorkPhaseWithSomeReadyCards() {
        Phase lastWorkPhase = getLastWorkPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.READY_SOME);
    }

    public void fillLastWorkPhaseToAlmostFullWithCardsInProgress() {
        Phase lastWorkPhase = getLastWorkPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.IN_PROGRESS_ALMOST_FULL);
    }

    public void fillLastWorkPhaseWithSomeAlmostReadyCards() {
        Phase lastWorkPhase = getLastWorkPhase();
        PhasePopulater.fillWithCards(lastWorkPhase, PhasePopulater.FillingType.ALMOST_READY_SOME);
    }

    public void removeAllButOneCardFromBacklog() {
        List<Card> backlogDeck = game.getBoard().getBacklogDeck();
        backlogDeck.removeAll(backlogDeck.subList(1, backlogDeck.size()));
    }

    public void addCardsWithMockPhasePointsToDevelopmentInProgress(Integer cardsToAdd) {
        Column developmentInProgressColumn = getColumn(ColumnName.DEVELOPMENT_IN_PROGRESS);
        for (Integer i = 0; i < cardsToAdd; i++) {
            developmentInProgressColumn.getCards().add(CardBuilder.aCard().withMockPhasePoints().build());
        }
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

        addCardsWithMockPhasePointsToDevelopmentInProgress(cardsToAddUntilFullEnough);
    }

    private Phase getLastWorkPhase() {
        return game.getBoard().getPhases().stream()
                .filter(Phase::getIsWorkPhase)
                .reduce((first, second) -> second)
                .orElse(null);
    }

    private List<Phase> getFirstWorkPhases() {
        List<Phase> workPhases = game.getBoard().getPhases().stream()
                .filter(Phase::getIsWorkPhase)
                .collect(Collectors.toList());
        Collections.reverse(workPhases);
        List<Phase> allButLastWorkPhase = workPhases.stream()
                .skip(1)
                .collect(Collectors.toList());
        return allButLastWorkPhase;
    }

}

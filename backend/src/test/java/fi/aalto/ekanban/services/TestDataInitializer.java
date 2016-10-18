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
import fi.aalto.ekanban.models.db.phases.Phase;

public class TestDataInitializer {

    public static Game initNormalDifficultyGame() {
        Phase analysisPhase = PhaseBuilder.aPhase().analysis().build();
        Phase developmentPhase = PhaseBuilder.aPhase().development().build();
        Phase testPhase = PhaseBuilder.aPhase().test().build();
        Phase deployedPhase = PhaseBuilder.aPhase().deployed().build();

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

        List<Card> backlogDeckCards = new ArrayList<>();

        for (Integer i = 0; i < 15; i=i+3) {
            backlogDeckCards.add(CardBuilder.aCard()
                    .withFinancialValue(FinancialValue.HIGH)
                    .withCardPhasePoints(cardPhasePoints)
                    .withSubscribesWhenDeployed("1")
                    .withOrderNumber(i+1)
                    .build());
            backlogDeckCards.add(CardBuilder.aCard()
                    .withFinancialValue(FinancialValue.MED)
                    .withCardPhasePoints(cardPhasePoints)
                    .withSubscribesWhenDeployed("1")
                    .withOrderNumber(i+2)
                    .build());
            backlogDeckCards.add(CardBuilder.aCard()
                    .withFinancialValue(FinancialValue.LOW)
                    .withCardPhasePoints(cardPhasePoints)
                    .withSubscribesWhenDeployed("1")
                    .withOrderNumber(i+3)
                    .build());
        }

        List<Phase> normalDifficultyPhases = Arrays.asList(analysisPhase, developmentPhase, testPhase, deployedPhase);

        Board gameBoard = BoardBuilder.aBoard()
                .withBacklogDeck(backlogDeckCards)
                .withPhases(normalDifficultyPhases)
                .build();

        return GameBuilder.aGame()
                .withBoard(gameBoard)
                .withPlayerName("player")
                .build();
    }

}

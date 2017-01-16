package fi.aalto.ekanban.services;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fi.aalto.ekanban.builders.CFDDailyValueBuilder;
import fi.aalto.ekanban.enums.TrackLinePlace;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.models.db.phases.Phase;
import fi.aalto.ekanban.models.CFDDailyValue;

@Service
public class CFDCalculatorService {
    public Game calculateCFDForCurrentDay(Game game) {
        checkCFD(game);
        game = calculateAndAddNewEntry(game);
        return game;
    }

    private void checkCFD(Game game) {
        if (gameHasDailyValuesUntilYesterday(game)) {
            throw new IllegalStateException("The CFD contains invalid amount of daily values");
        }
        checkForMissingPhasesInCFDDailyValues(game);
    }

    private boolean gameHasDailyValuesUntilYesterday(Game game) {
        return game.getCFD().getCfdDailyValues().size() != game.getCurrentDay();
    }

    private void checkForMissingPhasesInCFDDailyValues(Game game) {
        game.getBoard().getPhases().stream()
                .filter(phase -> phase.getTrackLinePlace() != null)
                .collect(Collectors.toList())
                .forEach(phase -> {
                    game.getCFD().getCfdDailyValues().forEach(cfdDailyValue -> {
                        if (!cfdDailyValue.getPhaseValues().containsKey(phase.getId())) {
                            throw new IllegalStateException("The CFD daily values have missing phase ids");
                        }
                    });
        });
    }

    private Game calculateAndAddNewEntry(Game game) {
        CFDDailyValue dailyValue = CFDDailyValueBuilder.aCFDDailyValue()
                .withDay(game.getCurrentDay())
                .withEnteredBoard(getCardAmountOnBoard(game))
                .withPhaseValues(getCardAmountsThatHavePassedTrackLines(game))
                .build();
        game.getCFD().getCfdDailyValues().add(dailyValue);
        return game;
    }

    private Map<String, Integer> getCardAmountsThatHavePassedTrackLines(Game game) {
        Map<String, Integer> cardAmountsThatHavePassedTrackLines = new HashMap<>();
        List<Integer> columnCardCounts = game.getBoard().getPhases().stream()
                .flatMap(phase -> phase.getColumns().stream())
                .mapToInt(column -> column.getCards().size())
                .boxed()
                .collect(Collectors.toList());
        Integer columnCursor = 0;
        for (Phase phase : game.getBoard().getPhases()) {
            Integer phaseColumnCount = phase.getColumns().size();
            if (phase.getTrackLinePlace() != null) {
                Integer colsBeforeTrackLine = getAmountOfColumnsBeforeTrackLineOfPhase(phase);
                cardAmountsThatHavePassedTrackLines.put(phase.getId(),
                        columnCardCountAfterCursor(columnCardCounts, columnCursor + colsBeforeTrackLine));
            }
            columnCursor += phaseColumnCount;
        }
        return cardAmountsThatHavePassedTrackLines;
    }

    private Integer columnCardCountAfterCursor(List<Integer> columnCardCounts, Integer columnCursor) {
        List<Integer> countsAfterCursor = columnCardCounts.subList(columnCursor, columnCardCounts.size());
        return countsAfterCursor.stream().mapToInt(Integer::intValue).sum();
    }

    private Integer getAmountOfColumnsBeforeTrackLineOfPhase(Phase phase) {
        if (phase.getTrackLinePlace().equals(TrackLinePlace.RIGHT)) {
            return phase.getColumns().size();
        }
        else if (phase.getTrackLinePlace().equals(TrackLinePlace.MIDDLE)) {
            if (phase.hasSecondColumn()) {
                return 1;
            }
            else {
                throw new IllegalStateException("trackLinePlace was MIDDLE but phase doesn't have two columns");
            }
        }
        else {
            return 0;
        }
    }

    private Integer getCardAmountOnBoard(Game game) {
        return game.getBoard().getPhases().stream().mapToInt(phase -> phase.getAllCards().size()).sum();
    }
}

package fi.aalto.ekanban.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import fi.aalto.ekanban.builders.CardBuilder;
import fi.aalto.ekanban.models.db.games.Card;
import fi.aalto.ekanban.models.db.games.CardPhasePoint;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

public class PhasePopulater {

    public enum FillingType {
        IN_PROGRESS_SOME, IN_PROGRESS_ALMOST_FULL, IN_PROGRESS_FULL,
        ALMOST_READY_SOME, ALMOST_READY_FULL,
        READY_SOME, READY_FULL
    }

    private enum Readiness {
        IN_PROGRESS, ALMOST_READY, READY
    }

    private enum Fullness {
        SOME, ALMOST_FULL, FULL
    }

    public static void fillWithCards(List<Phase> phases, FillingType fillingType) {
        phases.forEach(phase -> fillWithCards(phase, fillingType));
    }

    public static void fillWithCards(Phase phase, FillingType fillingType) {
        Integer amountOfCards = phase.getColumns().stream()
                .flatMapToInt(column -> IntStream.of(column.getCards().size()))
                .sum();
        if (amountOfCards > 0) {
            throw new IllegalStateException("The phase has cards already");
        }
        switch (fillingType) {
            case IN_PROGRESS_SOME:
                fillSome(phase, Readiness.IN_PROGRESS);
                break;
            case ALMOST_READY_SOME:
                fillSome(phase, Readiness.ALMOST_READY);
                break;
            case READY_SOME:
                fillSome(phase, Readiness.READY);
                break;
            case IN_PROGRESS_ALMOST_FULL:
                fillInProgressAlmostFull(phase);
                break;
            case IN_PROGRESS_FULL:
                fillInProgressFull(phase);
                break;
            case ALMOST_READY_FULL:
                fillAlmostReadyFull(phase);
                break;
            case READY_FULL:
                fillReadyFull(phase);
                break;
            default:
                throw new IllegalStateException("No such fillingType");
        }
    }

    private static void fillSome(Phase phase, Readiness readiness) {
        if (phase.getWipLimit().equals(1)) {
            throw new IllegalStateException("Cannot fill just some cards to a phase that has a WIP-limit of 1."
                    + "It becomes full already with the first card");
        }
        Card card = getCardWithPhasePoints(phase, readiness);
        Column firstColumn = phase.getColumns().get(0);
        firstColumn.getCards().add(card);
    }

    private static void fillInProgressAlmostFull(Phase phase) {
        List<Column> columns = phase.getColumns();
        List<Card> cards = getCardsToFill(phase, Fullness.ALMOST_FULL, Readiness.IN_PROGRESS);
        cards.forEach(card -> columns.get(0).getCards().add(card));
    }

    private static void fillInProgressFull(Phase phase) {
        List<Column> columns = phase.getColumns();
        List<Card> cards = getCardsToFill(phase, Fullness.FULL, Readiness.IN_PROGRESS);
        cards.forEach(card -> columns.get(0).getCards().add(card));
    }

    private static void fillAlmostReadyFull(Phase phase) {
        List<Column> columns = phase.getColumns();
        List<Card> cards = getCardsToFill(phase, Fullness.FULL, Readiness.ALMOST_READY);
        cards.forEach(card -> columns.get(0).getCards().add(card));
    }

    private static void fillReadyFull(Phase phase) {
        List<Column> columns = phase.getColumns();
        List<Card> cards = getCardsToFill(phase, Fullness.FULL, Readiness.READY);
        Integer amountOfColumns = columns.size();

        Integer columnIdxAlternator = 0;
        for (Integer i = 0; i < cards.size(); i++) {
            Card card = cards.get(i);
            Column column = columns.get(columnIdxAlternator);
            column.getCards().add(card);
            columnIdxAlternator = (columnIdxAlternator + 1) % amountOfColumns;
        }
    }

    private static List<Card> getCardsToFill(Phase phase, Fullness fullness, Readiness readiness) {
        Integer amountOfCardsToFill;
        switch (fullness) {
            case FULL:
                amountOfCardsToFill = phase.getWipLimit();
                break;
            case ALMOST_FULL:
                amountOfCardsToFill = phase.getWipLimit() - 1;
                break;
            case SOME:
                amountOfCardsToFill = 1;
                break;
            default:
                throw new IllegalStateException("No such fullness");
        }
        List<Card> cardsToFill = new ArrayList<>();
        for (Integer i = 0; i < amountOfCardsToFill; i++) {
            Card exampleCard = getCardWithPhasePoints(phase, readiness);
            cardsToFill.add(exampleCard);
        }
        return cardsToFill;
    }

    private static Card getCardWithPhasePoints(Phase phase, Readiness readiness) {
        Card card = CardBuilder.aCard()
                .withMockPhasePoints()
                .build();
        CardPhasePoint phasePoint = card.getCardPhasePoints().stream()
                .filter(p -> p.getPhaseId().equals(phase.getId()))
                .findFirst()
                .orElse(null);
        setPhasePointToCardState(phasePoint, readiness);
        return card;
    }

    private static void setPhasePointToCardState(CardPhasePoint phasePoint, Readiness readiness) {
        Integer totalPoints = 10;
        Integer almostReadyPoints = 9;
        Integer inProgressPoints = 5;
        Integer pointsDone;
        switch (readiness) {
            case IN_PROGRESS:
                pointsDone = inProgressPoints;
                break;
            case ALMOST_READY:
                pointsDone = almostReadyPoints;
                break;
            case READY:
                pointsDone = totalPoints;
                break;
            default:
                throw new IllegalStateException("No such readiness");
        }
        phasePoint.setTotalPoints(totalPoints);
        phasePoint.setPointsDone(pointsDone);
    }

}

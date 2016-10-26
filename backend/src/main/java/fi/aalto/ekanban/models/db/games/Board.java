package fi.aalto.ekanban.models.db.games;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.exceptions.CardNotFoundException;
import fi.aalto.ekanban.exceptions.ColumnNotFoundException;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.models.db.phases.Column;
import fi.aalto.ekanban.models.db.phases.Phase;

@Document
public class Board {

    @Id
    private String id;

    @Field
    private List<Card> backlogDeck;

    @Field
    private List<EventCard> eventCardDeck;

    @Field
    private List<Phase> phases;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Card> getBacklogDeck() {
        return backlogDeck;
    }

    public void setBacklogDeck(List<Card> backlogDeck) {
        this.backlogDeck = backlogDeck;
    }

    public List<EventCard> getEventCardDeck() {
        return eventCardDeck;
    }

    public void setEventCardDeck(List<EventCard> eventCardDeck) {
        this.eventCardDeck = eventCardDeck;
    }

    public List<Phase> getPhases() {
        return phases;
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Board)) return false;

        Board board = (Board) o;

        if (id != null ? !id.equals(board.id) : board.id != null) return false;
        if (backlogDeck != null ? !backlogDeck.equals(board.backlogDeck) : board.backlogDeck != null) return false;
        if (eventCardDeck != null ? !eventCardDeck.equals(board.eventCardDeck) : board.eventCardDeck != null)
            return false;
        return phases != null ? phases.equals(board.phases) : board.phases == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (backlogDeck != null ? backlogDeck.hashCode() : 0);
        result = 31 * result + (eventCardDeck != null ? eventCardDeck.hashCode() : 0);
        result = 31 * result + (phases != null ? phases.hashCode() : 0);
        return result;
    }

    public boolean isColumnNextAdjacent(String referenceColumnId, String inspectedOtherColumnId)
            throws ColumnNotFoundException {
        return isColumnNextAdjacentInsideSamePhase(referenceColumnId, inspectedOtherColumnId) ||
               isColumnNextAdjacentInAdjacentPhase(referenceColumnId, inspectedOtherColumnId);
    }

    public boolean isValid() {
        return phases != null && phases.stream().allMatch(Phase::isValid);
    }

    public Column getColumnWithId(String columnId) throws ColumnNotFoundException {
        for (Phase phase : phases) {
            Optional<Column> columnWithId = phase.getColumns().stream()
                    .filter(column -> column.getId().equals(columnId))
                    .findFirst();
            if (columnWithId.isPresent()) {
                return columnWithId.get();
            }
        }
        throw new ColumnNotFoundException(MessageFormat.format("Board has no column with id {0}", columnId));
    }

    public boolean doesMoveExceedWIP(MoveCardAction moveCardAction) throws ColumnNotFoundException {
        if (areColumnsInSamePhase(moveCardAction.getFromColumnId(), moveCardAction.getToColumnId())) {
            return false;
        }
        Phase phase = getPhaseWithColumn(moveCardAction.getToColumnId());
        return phase.isFullWip();
    }

    public boolean isCardInColumn(String cardId, String columnId) throws ColumnNotFoundException {
        Column column = getColumnWithId(columnId);
        return column.hasCard(cardId);
    }

    public void performMoveCardAction(MoveCardAction moveCardAction)
            throws ColumnNotFoundException, CardNotFoundException {
        Column fromColumn = getColumnWithId(moveCardAction.getFromColumnId());
        Column toColumn = getColumnWithId(moveCardAction.getToColumnId());
        Card cardToMove = fromColumn.pullCard(moveCardAction.getCardId());
        toColumn.pushCard(cardToMove);
    }

    private boolean isColumnNextAdjacentInsideSamePhase(String referenceColumnId, String inspectedOtherColumnId)
            throws ColumnNotFoundException {
        if (!areColumnsInSamePhase(referenceColumnId, inspectedOtherColumnId)) {
            return false;
        }
        Phase phase = getPhaseWithColumn(referenceColumnId);
        return phase.isColumnNextAdjacent(referenceColumnId, inspectedOtherColumnId);
    }

    private boolean areColumnsInSamePhase(String referenceColumnId, String inspectedOtherColumnId)
            throws ColumnNotFoundException {
        Integer refColPhaseNumber = getPhaseOrderNumberWithColumn(referenceColumnId);
        Integer adjColPhaseNumber = getPhaseOrderNumberWithColumn(inspectedOtherColumnId);
        return refColPhaseNumber.equals(adjColPhaseNumber);
    }

    private boolean isColumnNextAdjacentInAdjacentPhase(String referenceColumnId, String inspectedOtherColumnId)
            throws ColumnNotFoundException {
        if (!areColumnsInAdjacentPhases(referenceColumnId, inspectedOtherColumnId)) {
            return false;
        }
        else {
            Phase refColPhase = getPhaseWithColumn(referenceColumnId);
            Phase adjColPhase = getPhaseWithColumn(inspectedOtherColumnId);
            return refColPhase.isTheLastColumn(referenceColumnId) &&
                   adjColPhase.isTheFirstColumn(inspectedOtherColumnId);
        }
    }

    private boolean areColumnsInAdjacentPhases(String referenceColumnId, String inspectedOtherColumnId)
            throws ColumnNotFoundException {
        Integer refColPhaseNumber = getPhaseOrderNumberWithColumn(referenceColumnId);
        Integer adjColPhaseNumber = getPhaseOrderNumberWithColumn(inspectedOtherColumnId);
        return refColPhaseNumber == adjColPhaseNumber - 1;
    }

    private Phase getPhaseWithColumn(String columnId) throws ColumnNotFoundException {
        Integer phaseNumber = getPhaseOrderNumberWithColumn(columnId);
        return phases.get(phaseNumber);
    }

    private Integer getPhaseOrderNumberWithColumn(String columnId) throws ColumnNotFoundException {
        for (Integer i = 0; i < phases.size(); i++) {
            if (phases.get(i).containsColumnWithId(columnId)) {
                return i;
            }
        }
        throw new ColumnNotFoundException(MessageFormat.format("Board has no column with id {0}", columnId));
    }
}

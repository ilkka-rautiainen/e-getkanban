package fi.aalto.ekanban.models.db.games;

import static fi.aalto.ekanban.ApplicationConstants.DEPLOYED_PHASE_ID;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.exceptions.*;
import fi.aalto.ekanban.models.MoveCardAction;
import fi.aalto.ekanban.models.db.phases.*;

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
    @Field
    private String enteredBoardTrackLineColor;

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

    @JsonIgnore
    public List<Phase> getWorkPhases() {
        return phases.stream().filter(Phase::getIsWorkPhase).collect(Collectors.toList());
    }

    public void setPhases(List<Phase> phases) {
        this.phases = phases;
    }

    public String getEnteredBoardTrackLineColor() {
        return enteredBoardTrackLineColor;
    }

    public void setEnteredBoardTrackLineColor(String enteredBoardTrackLineColor) {
        this.enteredBoardTrackLineColor = enteredBoardTrackLineColor;
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
        if (enteredBoardTrackLineColor != null ? !enteredBoardTrackLineColor.equals(board.enteredBoardTrackLineColor) : board.enteredBoardTrackLineColor != null)
            return false;
        return phases != null ? phases.equals(board.phases) : board.phases == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (backlogDeck != null ? backlogDeck.hashCode() : 0);
        result = 31 * result + (eventCardDeck != null ? eventCardDeck.hashCode() : 0);
        result = 31 * result + (phases != null ? phases.hashCode() : 0);
        result = 31 * result + (enteredBoardTrackLineColor != null ? enteredBoardTrackLineColor.hashCode() : 0);
        return result;
    }

    public Boolean isColumnNextAdjacent(String referenceColumnId, String inspectedOtherColumnId) {
        return isColumnNextAdjacentInsideSamePhase(referenceColumnId, inspectedOtherColumnId) ||
               isColumnNextAdjacentInAdjacentPhase(referenceColumnId, inspectedOtherColumnId);
    }

    @JsonIgnore
    public Boolean isValid() {
        return phases != null && phases.stream().allMatch(Phase::isValid);
    }

    public Column getColumnWithId(String columnId) {
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

    public Boolean doesMoveExceedWIP(MoveCardAction moveCardAction) {
        if (areColumnsInSamePhase(moveCardAction.getFromColumnId(), moveCardAction.getToColumnId())) {
            return false;
        }
        Phase phase = getPhaseWithColumn(moveCardAction.getToColumnId());
        return phase.isFullWip();
    }

    public Boolean isCardInColumn(String cardId, String columnId) {
        Column column = getColumnWithId(columnId);
        return column.hasCard(cardId);
    }

    public Phase getPhaseWithId(String phaseId) {
        Optional<Phase> phaseOptional = phases.stream().filter(phase -> phase.getId().equals(phaseId)).findFirst();
        if (!phaseOptional.isPresent()) {
            throw new PhaseNotFoundException(MessageFormat.format("Board doesn't have phase with id {0}", phaseId));
        }
        return phaseOptional.get();
    }

    public Boolean hasNextPhase(Phase inspectedPhase) {
        if (!getPhases().contains(inspectedPhase)) {
            throw new PhaseNotFoundException(
                    MessageFormat.format("Board doesn't have phase with id {0}", inspectedPhase.getId()));
        }
        Integer lastPhaseIdx = getPhases().size() - 1;
        return getPhases().indexOf(inspectedPhase) < lastPhaseIdx;
    }

    public Phase getNextPhase(Phase phase) {
        if (!hasNextPhase(phase)) {
            return null;
        }
        Integer phaseIdx = getPhases().indexOf(phase);
        return getPhases().get(phaseIdx + 1);
    }

    public boolean backlogDeckEmpty() {
        return getBacklogDeck().isEmpty();
    }

    public boolean allCardsDeployed() {
        return getPhases().stream()
                .filter(phase -> !phase.getId().equals(DEPLOYED_PHASE_ID))
                .flatMap(phase -> phase.getAllCards().stream())
                .collect(Collectors.toList())
                .isEmpty();
    }

    private boolean isColumnNextAdjacentInsideSamePhase(String referenceColumnId, String inspectedOtherColumnId) {
        if (!areColumnsInSamePhase(referenceColumnId, inspectedOtherColumnId)) {
            return false;
        }
        Phase phase = getPhaseWithColumn(referenceColumnId);
        return phase.isColumnNextAdjacent(referenceColumnId, inspectedOtherColumnId);
    }

    private Boolean areColumnsInSamePhase(String referenceColumnId, String inspectedOtherColumnId) {
        Integer refColPhaseNumber = getPhaseOrderNumberWithColumn(referenceColumnId);
        Integer adjColPhaseNumber = getPhaseOrderNumberWithColumn(inspectedOtherColumnId);
        return refColPhaseNumber.equals(adjColPhaseNumber);
    }

    private Boolean isColumnNextAdjacentInAdjacentPhase(String referenceColumnId, String inspectedOtherColumnId) {
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

    private Boolean areColumnsInAdjacentPhases(String referenceColumnId, String inspectedOtherColumnId) {
        Integer refColPhaseNumber = getPhaseOrderNumberWithColumn(referenceColumnId);
        Integer adjColPhaseNumber = getPhaseOrderNumberWithColumn(inspectedOtherColumnId);
        return refColPhaseNumber == adjColPhaseNumber - 1;
    }

    private Phase getPhaseWithColumn(String columnId) {
        Integer phaseNumber = getPhaseOrderNumberWithColumn(columnId);
        return phases.get(phaseNumber);
    }

    private Integer getPhaseOrderNumberWithColumn(String columnId) {
        for (Integer i = 0; i < phases.size(); i++) {
            if (phases.get(i).containsColumnWithId(columnId)) {
                return i;
            }
        }
        throw new ColumnNotFoundException(MessageFormat.format("Board has no column with id {0}", columnId));
    }
}

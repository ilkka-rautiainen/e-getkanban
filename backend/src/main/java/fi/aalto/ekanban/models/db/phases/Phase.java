package fi.aalto.ekanban.models.db.phases;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.exceptions.ColumnNotFoundException;

@Document
public class Phase {

    @Id
    private String id;

    @Field
    private List<Column> columns;

    @Field
    private Integer wipLimit;

    @Field
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public Integer getWipLimit() {
        return wipLimit;
    }

    public void setWipLimit(Integer wipLimit) {
        this.wipLimit = wipLimit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Phase)) return false;

        Phase phase = (Phase) o;

        if (id != null ? !id.equals(phase.id) : phase.id != null) return false;
        if (columns != null ? !columns.equals(phase.columns) : phase.columns != null) return false;
        if (wipLimit != null ? !wipLimit.equals(phase.wipLimit) : phase.wipLimit != null) return false;
        return name != null ? name.equals(phase.name) : phase.name == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (columns != null ? columns.hashCode() : 0);
        result = 31 * result + (wipLimit != null ? wipLimit.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    public Boolean containsColumnWithId(String columnId) {
        return columns.stream().anyMatch(column -> column.getId().equals(columnId));
    }

    public Boolean isColumnNextAdjacent(String referenceColumnId, String inspectedOtherColumnId)
            throws ColumnNotFoundException {
        Column referenceColumn = getColumnById(referenceColumnId);
        Column inspectedOtherColumn = getColumnById(inspectedOtherColumnId);
        return columns.indexOf(referenceColumn) == columns.indexOf(inspectedOtherColumn) - 1;
    }

    public Boolean isTheLastColumn(String columnId) throws ColumnNotFoundException {
        Column column = getColumnById(columnId);
        return columns.indexOf(column) == columns.size() - 1;
    }

    public Boolean isTheFirstColumn(String columnId) throws ColumnNotFoundException {
        Column column = getColumnById(columnId);
        return columns.indexOf(column) == 0;
    }

    @JsonIgnore
    public Boolean isFullWip() {
        if (wipLimit == null) {
            return false;
        }
        return getTotalAmountOfCards() >= wipLimit;
    }

    public Integer getTotalAmountOfCards() {
        return columns.stream().collect(Collectors.summingInt(column -> column.getCards().size()));
    }

    public Boolean isValid() {
        return columns != null && columns.stream().allMatch(Column::isValid);
    }

    private Column getColumnById(String columnId) throws ColumnNotFoundException {
        Optional<Column> result = columns.stream()
                .filter(column -> column.getId().equals(columnId))
                .findFirst();
        if (!result.isPresent()) {
            throw new ColumnNotFoundException(
                    MessageFormat.format("Phase with id {0} doesn''t have column with id {1}", getId(), columnId));
        }
        return result.get();
    }
}

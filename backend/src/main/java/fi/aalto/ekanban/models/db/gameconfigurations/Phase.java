package fi.aalto.ekanban.models.db.gameconfigurations;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.models.db.games.Column;

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
}

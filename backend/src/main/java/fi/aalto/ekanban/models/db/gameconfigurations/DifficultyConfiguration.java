package fi.aalto.ekanban.models.db.gameconfigurations;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import fi.aalto.ekanban.enums.GameDifficulty;

@Document
public class DifficultyConfiguration {

    @Id
    private String id;
    @Field
    private GameDifficulty gameDifficulty;
    @Field
    private List<GameOptionChange> initialGameOptionChanges;
    @Field
    private List<EventCardInstantiator> eventCardConfigurations;
    @Field
    private List<String> backlogCardIds;
    @Field
    private String boardEnteredTracklineColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GameDifficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficulty(GameDifficulty gameDifficulty) {
        this.gameDifficulty = gameDifficulty;
    }

    public List<GameOptionChange> getInitialGameOptionChanges() {
        return initialGameOptionChanges;
    }

    public void setInitialGameOptionChanges(List<GameOptionChange> initialGameOptionChanges) {
        this.initialGameOptionChanges = initialGameOptionChanges;
    }

    public List<EventCardInstantiator> getEventCardConfigurations() {
        return eventCardConfigurations;
    }

    public void setEventCardConfigurations(List<EventCardInstantiator> eventCardConfigurations) {
        this.eventCardConfigurations = eventCardConfigurations;
    }

    public List<String> getBacklogCardIds() {
        return backlogCardIds;
    }

    public void setBacklogCardIds(List<String> backlogCardIds) {
        this.backlogCardIds = backlogCardIds;
    }

    public String getBoardEnteredTracklineColor() {
        return boardEnteredTracklineColor;
    }

    public void setBoardEnteredTracklineColor(String boardEnteredTracklineColor) {
        this.boardEnteredTracklineColor = boardEnteredTracklineColor;
    }

    @JsonIgnore
    public boolean isNormal() {
        return getId().equals(GameDifficulty.NORMAL.toString());
    }

    @JsonIgnore
    public boolean isMedium() {
        return getId().equals(GameDifficulty.MEDIUM.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof DifficultyConfiguration)) return false;

        DifficultyConfiguration that = (DifficultyConfiguration) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (gameDifficulty != null ? !gameDifficulty.equals(that.gameDifficulty) : that.gameDifficulty != null) return false;
        if (initialGameOptionChanges != null ? !initialGameOptionChanges.equals(that.initialGameOptionChanges) : that.initialGameOptionChanges != null)
            return false;
        if (eventCardConfigurations != null ? !eventCardConfigurations.equals(that.eventCardConfigurations) : that.eventCardConfigurations != null)
            return false;
        if (boardEnteredTracklineColor != null ? !boardEnteredTracklineColor.equals(that.boardEnteredTracklineColor) : that.boardEnteredTracklineColor != null)
            return false;
        return backlogCardIds != null ? backlogCardIds.equals(that.backlogCardIds) : that.backlogCardIds == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (gameDifficulty != null ? gameDifficulty.hashCode() : 0);
        result = 31 * result + (initialGameOptionChanges != null ? initialGameOptionChanges.hashCode() : 0);
        result = 31 * result + (eventCardConfigurations != null ? eventCardConfigurations.hashCode() : 0);
        result = 31 * result + (backlogCardIds != null ? backlogCardIds.hashCode() : 0);
        result = 31 * result + (boardEnteredTracklineColor != null ? boardEnteredTracklineColor.hashCode() : 0);
        return result;
    }
}

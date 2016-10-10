package fi.aalto.ekanban.builders;

import fi.aalto.ekanban.models.db.gameconfigurations.GameOptionChange;

public final class GameOptionChangeBuilder {
    private String id;
    private String methodName;
    private String parameters;

    private GameOptionChangeBuilder() {
    }

    public static GameOptionChangeBuilder aGameOptionChange() {
        return new GameOptionChangeBuilder();
    }

    public GameOptionChangeBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public GameOptionChangeBuilder withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    public GameOptionChangeBuilder withParameters(String parameters) {
        this.parameters = parameters;
        return this;
    }

    public GameOptionChange build() {
        GameOptionChange gameOptionChange = new GameOptionChange();
        gameOptionChange.setId(id);
        gameOptionChange.setMethodName(methodName);
        gameOptionChange.setParameters(parameters);
        return gameOptionChange;
    }
}

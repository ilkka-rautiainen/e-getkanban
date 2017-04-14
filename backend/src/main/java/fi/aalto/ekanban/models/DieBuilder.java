package fi.aalto.ekanban.models;


import fi.aalto.ekanban.builders.Die;

public final class DieBuilder {
    private Integer primaryValue;
    private Integer firstSecondaryValue;
    private Integer secondSecondaryValue;

    private DieBuilder() {
    }

    public static DieBuilder aDie() {
        return new DieBuilder();
    }

    public DieBuilder withPrimaryValue(Integer primaryValue) {
        this.primaryValue = primaryValue;
        return this;
    }

    public DieBuilder withFirstSecondaryValue(Integer firstSecondaryValue) {
        this.firstSecondaryValue = firstSecondaryValue;
        return this;
    }

    public DieBuilder withSecondSecondaryValue(Integer secondSecondaryValue) {
        this.secondSecondaryValue = secondSecondaryValue;
        return this;
    }

    public Die build() {
        Die die = new Die();
        die.setPrimaryValue(primaryValue);
        die.setFirstSecondaryValue(firstSecondaryValue);
        die.setSecondSecondaryValue(secondSecondaryValue);
        return die;
    }
}

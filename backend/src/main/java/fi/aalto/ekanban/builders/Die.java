package fi.aalto.ekanban.builders;

public class Die {

    private Integer primaryValue;
    private Integer firstSecondaryValue;
    private Integer secondSecondaryValue;

    public Integer getPrimaryValue() {
        return primaryValue;
    }

    public void setPrimaryValue(Integer primaryValue) {
        this.primaryValue = primaryValue;
    }

    public Integer getFirstSecondaryValue() {
        return firstSecondaryValue;
    }

    public void setFirstSecondaryValue(Integer firstSecondaryValue) {
        this.firstSecondaryValue = firstSecondaryValue;
    }

    public Integer getSecondSecondaryValue() {
        return secondSecondaryValue;
    }

    public void setSecondSecondaryValue(Integer secondSecondaryValue) {
        this.secondSecondaryValue = secondSecondaryValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        if (!(o instanceof Die)) return false;

        Die die = (Die) o;

        if (primaryValue != null ? !primaryValue.equals(die.primaryValue) : die.primaryValue != null) return false;
        if (firstSecondaryValue != null ? !firstSecondaryValue.equals(die.firstSecondaryValue) : die.firstSecondaryValue != null)
            return false;
        return secondSecondaryValue != null ? secondSecondaryValue.equals(die.secondSecondaryValue) : die.secondSecondaryValue == null;

    }

    @Override
    public int hashCode() {
        int result = primaryValue != null ? primaryValue.hashCode() : 0;
        result = 31 * result + (firstSecondaryValue != null ? firstSecondaryValue.hashCode() : 0);
        result = 31 * result + (secondSecondaryValue != null ? secondSecondaryValue.hashCode() : 0);
        return result;
    }
}

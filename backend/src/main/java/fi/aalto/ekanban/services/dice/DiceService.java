package fi.aalto.ekanban.services.dice;

@FunctionalInterface
public interface DiceService {
    Integer castDie(Integer min, Integer max);
}

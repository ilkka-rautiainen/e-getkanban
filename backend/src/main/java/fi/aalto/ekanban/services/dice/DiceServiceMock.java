package fi.aalto.ekanban.services.dice;

import fi.aalto.ekanban.services.dice.DiceService;

public class DiceServiceMock implements DiceService {
    public Integer castDie(Integer min, Integer max) {
        return 5;
    }
}

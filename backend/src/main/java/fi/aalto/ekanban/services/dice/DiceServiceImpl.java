package fi.aalto.ekanban.services.dice;

import java.util.Random;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class DiceServiceImpl implements DiceService {
    public Integer castDie(Integer min, Integer max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}

package fi.aalto.ekanban.services.AssignResourcesAI;

import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class DiceService {
    public Integer cast(Integer min, Integer max) {
        Random random = new Random();
        return random.nextInt(max - min + 1) + min;
    }
}

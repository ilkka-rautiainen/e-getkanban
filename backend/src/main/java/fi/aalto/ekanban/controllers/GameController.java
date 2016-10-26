package fi.aalto.ekanban.controllers;

import static fi.aalto.ekanban.ApplicationConstants.GAME_PATH;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import fi.aalto.ekanban.enums.GameDifficulty;
import fi.aalto.ekanban.models.db.games.Game;
import fi.aalto.ekanban.services.GameService;

@RestController
@RequestMapping(GAME_PATH)
@Validated
public class GameController {

    @Autowired
    GameService gameService;

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public Game startGame(@Size(max = 128, message = "Name should be at most 128 characters long")
                          @RequestParam("playerName") String playerName,
                          @RequestParam("difficultyLevel") GameDifficulty gameDifficulty) {
        return gameService.startGame(playerName, gameDifficulty);
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleTooLongPlayerNameException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations ) {
            strBuilder.append(violation.getMessage() + "\n");
        }
        return "{\"status\":400,\"error\":\"Bad request\",\"message\":\""+strBuilder.toString()+"\"}";
    }

}

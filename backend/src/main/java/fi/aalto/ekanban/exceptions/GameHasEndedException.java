package fi.aalto.ekanban.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The game has already ended")
public class GameHasEndedException extends RuntimeException {
    public GameHasEndedException() { super(); }
    public GameHasEndedException(String message) { super(message); }
    public GameHasEndedException(String message, Throwable cause) { super(message, cause); }
    public GameHasEndedException(Throwable cause) { super(cause); }
}

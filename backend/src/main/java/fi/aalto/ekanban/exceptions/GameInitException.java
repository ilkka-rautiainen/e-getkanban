package fi.aalto.ekanban.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "The game couldn't be initialized")
public class GameInitException extends RuntimeException {
    public GameInitException() { super(); }
    public GameInitException(String message) { super(message); }
    public GameInitException(String message, Throwable cause) { super(message, cause); }
    public GameInitException(Throwable cause) { super(cause); }
}

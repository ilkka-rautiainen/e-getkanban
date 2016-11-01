package fi.aalto.ekanban.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND, reason = "Game wasn't found")
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException() { super(); }
    public GameNotFoundException(String message) { super(message); }
    public GameNotFoundException(String message, Throwable cause) { super(message, cause); }
    public GameNotFoundException(Throwable cause) { super(cause); }
}

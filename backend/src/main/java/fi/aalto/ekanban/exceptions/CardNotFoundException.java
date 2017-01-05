package fi.aalto.ekanban.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was a missing card")
public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException() { super(); }
    public CardNotFoundException(String message) { super(message); }
    public CardNotFoundException(String message, Throwable cause) { super(message, cause); }
    public CardNotFoundException(Throwable cause) { super(cause); }
}

package fi.aalto.ekanban.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There's a missing phase in the game configuration")
public class PhaseNotFoundException extends RuntimeException {
    public PhaseNotFoundException() { super(); }
    public PhaseNotFoundException(String message) { super(message); }
    public PhaseNotFoundException(String message, Throwable cause) { super(message, cause); }
    public PhaseNotFoundException(Throwable cause) { super(cause); }
}

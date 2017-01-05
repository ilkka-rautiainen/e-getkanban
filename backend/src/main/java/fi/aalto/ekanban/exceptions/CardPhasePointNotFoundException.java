package fi.aalto.ekanban.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was a missing card phase point for a phase")
public class CardPhasePointNotFoundException extends RuntimeException {
    public CardPhasePointNotFoundException() { super(); }
    public CardPhasePointNotFoundException(String message) { super(message); }
    public CardPhasePointNotFoundException(String message, Throwable cause) { super(message, cause); }
    public CardPhasePointNotFoundException(Throwable cause) { super(cause); }
}

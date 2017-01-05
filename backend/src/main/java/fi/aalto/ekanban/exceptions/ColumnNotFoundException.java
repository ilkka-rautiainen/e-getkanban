package fi.aalto.ekanban.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "There was a missing column")
public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException() { super(); }
    public ColumnNotFoundException(String message) { super(message); }
    public ColumnNotFoundException(String message, Throwable cause) { super(message, cause); }
    public ColumnNotFoundException(Throwable cause) { super(cause); }
}

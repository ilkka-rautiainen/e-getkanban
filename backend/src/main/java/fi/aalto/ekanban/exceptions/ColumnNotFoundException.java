package fi.aalto.ekanban.exceptions;

public class ColumnNotFoundException extends Exception {
    public ColumnNotFoundException() { super(); }
    public ColumnNotFoundException(String message) { super(message); }
    public ColumnNotFoundException(String message, Throwable cause) { super(message, cause); }
    public ColumnNotFoundException(Throwable cause) { super(cause); }
}

package fi.aalto.ekanban.exceptions;

public class CardNotFoundException extends Exception {
    public CardNotFoundException() { super(); }
    public CardNotFoundException(String message) { super(message); }
    public CardNotFoundException(String message, Throwable cause) { super(message, cause); }
    public CardNotFoundException(Throwable cause) { super(cause); }
}

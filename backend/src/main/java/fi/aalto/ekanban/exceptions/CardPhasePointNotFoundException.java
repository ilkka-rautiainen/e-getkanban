package fi.aalto.ekanban.exceptions;

public class CardPhasePointNotFoundException extends Exception {
    public CardPhasePointNotFoundException() { super(); }
    public CardPhasePointNotFoundException(String message) { super(message); }
    public CardPhasePointNotFoundException(String message, Throwable cause) { super(message, cause); }
    public CardPhasePointNotFoundException(Throwable cause) { super(cause); }
}

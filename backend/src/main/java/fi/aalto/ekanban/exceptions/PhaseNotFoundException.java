package fi.aalto.ekanban.exceptions;

public class PhaseNotFoundException extends Exception {
    public PhaseNotFoundException() { super(); }
    public PhaseNotFoundException(String message) { super(message); }
    public PhaseNotFoundException(String message, Throwable cause) { super(message, cause); }
    public PhaseNotFoundException(Throwable cause) { super(cause); }
}

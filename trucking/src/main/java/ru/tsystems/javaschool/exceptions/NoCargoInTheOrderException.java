package ru.tsystems.javaschool.exceptions;

public class NoCargoInTheOrderException extends Exception {

    public NoCargoInTheOrderException() {
        super();
    }

    public NoCargoInTheOrderException(String message) {
        super(message);
    }

    public NoCargoInTheOrderException(Throwable cause) {
        super(cause);
    }

    public NoCargoInTheOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoCargoInTheOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

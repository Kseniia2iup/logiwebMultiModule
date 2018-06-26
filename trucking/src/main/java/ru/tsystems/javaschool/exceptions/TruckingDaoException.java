package ru.tsystems.javaschool.exceptions;

public class TruckingDaoException extends Exception {

    public TruckingDaoException() {
        super();
    }

    public TruckingDaoException(String message) {
        super(message);
    }

    public TruckingDaoException(Throwable cause) {
        super(cause);
    }

    public TruckingDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public TruckingDaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

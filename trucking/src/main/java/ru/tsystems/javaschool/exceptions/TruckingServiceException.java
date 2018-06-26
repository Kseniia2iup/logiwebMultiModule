package ru.tsystems.javaschool.exceptions;

public class TruckingServiceException extends Exception {

    public TruckingServiceException() {
        super();
    }

    public TruckingServiceException(String message) {
        super(message);
    }

    public TruckingServiceException(Throwable cause) {
        super(cause);
    }

    public TruckingServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TruckingServiceException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

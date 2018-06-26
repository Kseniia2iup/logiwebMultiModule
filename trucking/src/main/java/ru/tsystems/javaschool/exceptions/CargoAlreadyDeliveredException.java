package ru.tsystems.javaschool.exceptions;

public class CargoAlreadyDeliveredException extends Exception {

    public CargoAlreadyDeliveredException() {
        super();
    }

    public CargoAlreadyDeliveredException(String message) {
        super(message);
    }

    public CargoAlreadyDeliveredException(Throwable cause) {
        super(cause);
    }

    public CargoAlreadyDeliveredException(String message, Throwable cause) {
        super(message, cause);
    }

    public CargoAlreadyDeliveredException(String message, Throwable cause,
                                    boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

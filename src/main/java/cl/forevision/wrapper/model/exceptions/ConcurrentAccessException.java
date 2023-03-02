package cl.forevision.wrapper.model.exceptions;

public class ConcurrentAccessException extends Exception {

    public ConcurrentAccessException(String msg) {
        super(msg);
    }
}

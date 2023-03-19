package asc.portfolio.ascSb.cafe.exception;

public class CafeNotFoundException extends RuntimeException {
    public CafeNotFoundException() {
        super();
    }

    public CafeNotFoundException(String message) {
        super(message);
    }

    public CafeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CafeNotFoundException(Throwable cause) {
        super(cause);
    }

    protected CafeNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

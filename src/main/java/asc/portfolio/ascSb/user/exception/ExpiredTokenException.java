package asc.portfolio.ascSb.user.exception;

public class ExpiredTokenException extends TokenException {

    private final String subject;

    public ExpiredTokenException(String subject, String message) {
        super(message);
        this.subject = subject;
    }

    public ExpiredTokenException(String subject, String message, Throwable cause) {
        super(message, cause);
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }
}

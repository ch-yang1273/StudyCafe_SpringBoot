package asc.portfolio.ascSb.user.exception;

import asc.portfolio.ascSb.common.dto.TokenPayload;

public class ExpiredTokenException extends TokenException {

    private final TokenPayload payload;

    public ExpiredTokenException(TokenPayload payload, String message) {
        super(message);
        this.payload = payload;
    }

    public ExpiredTokenException(TokenPayload payload, String message, Throwable cause) {
        super(message, cause);
        this.payload = payload;
    }

    public TokenPayload getPayload() {
        return payload;
    }
}

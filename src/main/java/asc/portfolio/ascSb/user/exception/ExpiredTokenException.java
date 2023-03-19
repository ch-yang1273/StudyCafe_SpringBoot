package asc.portfolio.ascSb.user.exception;

import asc.portfolio.ascSb.common.dto.TokenPayload;

public class ExpiredTokenException extends TokenException {

    private final TokenPayload payload;

    public ExpiredTokenException(TokenPayload payload, UserErrorData data) {
        super(data);
        this.payload = payload;
    }

    public TokenPayload getPayload() {
        return payload;
    }
}

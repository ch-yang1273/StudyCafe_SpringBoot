package asc.portfolio.ascSb.common.auth.jwt;

import asc.portfolio.ascSb.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope //하나의 웹 요청 안에서만 살아있는 빈 Scope
@Getter
@Setter
public class AuthenticationContext {

    private User principal;

    public boolean isExist() {
        return principal != null;
    }
}

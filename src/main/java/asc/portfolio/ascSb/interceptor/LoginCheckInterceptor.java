package asc.portfolio.ascSb.interceptor;

import asc.portfolio.ascSb.user.service.UserAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final UserAuthService userAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        request.setAttribute("payload", userAuthService.checkAccessToken(token));
        return true;
    }
}

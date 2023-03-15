package asc.portfolio.ascSb.common.interceptor;

import asc.portfolio.ascSb.user.exception.TokenException;
import asc.portfolio.ascSb.user.service.UserAuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final UserAuthService userAuthService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            request.setAttribute("payload", userAuthService.checkAccessToken(token));
            // Controller 진행
            return true;
        } catch (TokenException e) {
            responseUnauthorized(response, e);
            // Controller 진행 중지
            return false;
        }
    }

    private void responseUnauthorized(HttpServletResponse response, TokenException e) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        Map<String, String> map = new HashMap<>();
        map.put("message", e.getMessage());
        String result = objectMapper.writeValueAsString(map);

        response.getWriter().write(result);
    }
}

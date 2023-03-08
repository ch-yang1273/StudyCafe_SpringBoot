package asc.portfolio.ascSb.common.interceptor;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.auth.jwt.AuthenticationContext;
import asc.portfolio.ascSb.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final AuthenticationContext authenticationContext;

    private final UserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String jwt = request.getHeader(HttpHeaders.AUTHORIZATION);

        try {
            User findUser = userService.checkAccessToken(jwt);

            log.info("Authorized User={}", findUser.getLoginId());
            authenticationContext.setPrincipal(findUser);

            // Controller 진행
            return true;
        } catch (JwtException e) {
            log.debug("Unauthorized access = {}", request.getRequestURL());

            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            Map<String, String> map = new HashMap<>();
            map.put("message", e.getMessage());
            String result = objectMapper.writeValueAsString(map);

            response.getWriter().write(result);

            // Controller 진행 중지
            return false;
        }
    }
}

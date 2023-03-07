package asc.portfolio.ascSb.common.auth.jwt;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

  private final AuthenticationContext authenticationContext;
  private final UserService userService;

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    //지원여부 확인

    log.info("Check supportsParameter");

    //"@LoginUser" User user
    boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
    //@LoginUser "User" user
    boolean hasUserType = User.class.isAssignableFrom(parameter.getParameterType());

    return hasLoginUserAnnotation && hasUserType;
  }

  @Override
  public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                WebDataBinderFactory binderFactory) throws Exception {
    if (authenticationContext.isExist()) {
      // LoginCheckInterceptor 에서 이미 처리되어 있는 경우
      log.info("return exist principal");

      return authenticationContext.getPrincipal();
    } else {
      // LoginCheckInterceptor 제외 된 경로
      log.info("return principal");
      String jwt = webRequest.getHeader(HttpHeaders.AUTHORIZATION);

      try {
        return userService.checkAccessToken(jwt);
      } catch (JwtException e) {
        return null;
      }
    }
  }

}

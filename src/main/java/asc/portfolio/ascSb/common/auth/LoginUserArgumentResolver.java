package asc.portfolio.ascSb.common.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //"@LoginUser" User user
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginUser.class);
        //@LoginUser "User" user
        boolean hasUserType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginUserAnnotation && hasUserType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        return Optional.ofNullable(Objects.requireNonNull(request).getAttribute("payload"))
                .orElseThrow(() -> new IllegalArgumentException("Authorization server error"));
    }

}

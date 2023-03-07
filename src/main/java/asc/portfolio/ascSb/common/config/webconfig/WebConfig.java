package asc.portfolio.ascSb.common.config.webconfig;
import asc.portfolio.ascSb.common.auth.jwt.LoginUserArgumentResolver;
import asc.portfolio.ascSb.common.interceptor.LoginCheckInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final LoginCheckInterceptor loginCheckInterceptor;
  private final LoginUserArgumentResolver loginUserArgumentResolver;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginCheckInterceptor)
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns(
                    "/api/v1/user/signup", "/api/v1/user/login", "/api/v1/user/reissue", "/api/v1/user/login-test", "/api/v1/cafe/state/**",
                    "/error", "/favicon.ico", "/",
                    "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**");
  }

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(loginUserArgumentResolver);
  }

  // 모든 uri에 대해 http://localhost:8080 도메인은 접근을 허용한다. (배포시 맞는 도메인 주소로 교체) @CrossOrigin(origins = "*") 사용X
  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOrigins("http://localhost:8080")
            .allowedMethods("*");
  }
}

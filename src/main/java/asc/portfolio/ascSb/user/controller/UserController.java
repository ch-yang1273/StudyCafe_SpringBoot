package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.common.auth.jwt.LoginUser;
import asc.portfolio.ascSb.common.auth.jwt.JwtService;
import asc.portfolio.ascSb.user.dto.*;
import asc.portfolio.ascSb.user.service.UserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

  private final UserService userService;
  private final JwtService jwtService;

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> jwtExExHandle(JwtException ex) {
        log.info("JwtException ex", ex);
        return new ResponseEntity<>("Invalid jwt", HttpStatus.UNAUTHORIZED);
    }

  @PostMapping("/signup")
  public ResponseEntity<String> singUp(@RequestBody @Valid UserSignupDto signUpDto, BindingResult bindingResult) {

    log.info("try signup. LoginId={}", signUpDto.getLoginId());

    if (bindingResult.hasErrors()) {
      log.error("Invalid Signup Format");
      return new ResponseEntity<>("BAD_REQUEST", HttpStatus.BAD_REQUEST);
    }

    try {
      Long signUpUserID = userService.signUp(signUpDto);
    } catch (Exception e) {
      log.error("Signup Unique violation");
      return new ResponseEntity<>("BAD_REQUEST - Unique violation", HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

  @PostMapping("/login")
  public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto loginDto, BindingResult bindingResult)
          throws Exception {

    log.info("try login. LoginId={}", loginDto.getLoginId());

    if (bindingResult.hasErrors()) {
      log.error("Validation error : Login DTO");
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

//    UserLoginResponseDto loginRespDto = jwtService.createTokenWithLogin(loginDto.getLoginId(), loginDto.getPassword());
    UserLoginResponseDto loginRespDto = userService.checkPassword(loginDto.getLoginId(), loginDto.getPassword());

    if (loginRespDto == null) {
      log.error("Unknown User");
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    log.info("Response Login Dto");
    return new ResponseEntity<>(loginRespDto, HttpStatus.OK);
  }

  @GetMapping("/login-check") //Test
  public ResponseEntity<String> loginCheck(@LoginUser User user) {

    log.info("user.getId()={}", user.getId());
    log.info("user.getLoginId()={}", user.getLoginId());
    log.info("user.getEmail()={}", user.getEmail());

    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

  @GetMapping("/login-test") //Test
  public ResponseEntity<String> loginCheckWithoutInterceptor(@LoginUser User user) {
    // LoginCheckInterceptor 를 통과하지 않은 Controller

    if (user != null) {
      log.info("user.getId()={}", user.getId());
      log.info("user.getLoginId()={}", user.getLoginId());
      log.info("user.getEmail()={}", user.getEmail());
    } else {
      log.info("user=null");
    }
    return new ResponseEntity<>("OK", HttpStatus.OK);
  }

  //
  @PostMapping("/reissue")
  public ResponseEntity<UserLoginResponseDto> reissueToken(@RequestBody @Valid UserTokenRequestDto tokenRequestDto) {
    log.debug("try reissue token");
    return new ResponseEntity<>(userService.reissueToken(tokenRequestDto), HttpStatus.OK);
  }

  @GetMapping("/qr-name")
  public ResponseEntity<UserQrAndNameResponseDto> userQrAndNameInfo(@LoginUser User user) {

    if (user == null) {
      log.error("유효하지 않은 로그인 입니다.");
      return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity<>(userService.userQrAndName(user.getId()), HttpStatus.OK);
  }

  @GetMapping("/admin/check")
  public ResponseEntity<UserForAdminResponseDto> adminCheckUserInfo(@LoginUser User user, @RequestParam String userLoginId) {

    if(user.getRole() != UserRoleType.ADMIN) {
      log.error("관리자 계정이 아닙니다.");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (userLoginId != null) {
      return new ResponseEntity<>(userService.AdminCheckUserInfo(userLoginId), HttpStatus.OK);
    } else {
      log.error("검색하고자 하는 id를 찾을수 없습니다.");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  @GetMapping("/admin/check/user-id")
  public ResponseEntity<?> adminCheckUserLoginId(@RequestParam String userLoginId) {
      boolean checkUserId = userService.checkLoginId(userLoginId);
      if (checkUserId) {
        log.info("exist user id");
        return new ResponseEntity<>("OK", HttpStatus.OK);
      }
      return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
  }
}

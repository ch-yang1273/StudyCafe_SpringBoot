package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.common.auth.jwt.LoginUser;
import asc.portfolio.ascSb.user.dto.*;
import asc.portfolio.ascSb.user.exception.TokenException;
import asc.portfolio.ascSb.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> tokenExExHandle(TokenException ex) {
        log.debug("TokenException ex", ex);
        return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
    }

    private String validateSingUpDto(BindingResult bindingResult) {
        Map<String, String> map = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            map.put(error.getField(), error.getDefaultMessage());
        }

        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException("JsonProcessingException", ex);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<String> singUp(@RequestBody @Valid UserSignupDto signUpDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(validateSingUpDto(bindingResult), HttpStatus.BAD_REQUEST);
        }

        try {
            userService.signUp(signUpDto);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Unique violation", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto loginDto) {
        UserLoginResponseDto loginRespDto = userService.checkPassword(loginDto.getLoginId(), loginDto.getPassword());
        return new ResponseEntity<>(loginRespDto, HttpStatus.OK);
    }

    @GetMapping("/login-check") //Test
    public ResponseEntity<Void> loginCheck(@LoginUser User user) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/login-test") //Test
    public ResponseEntity<String> loginCheckWithoutInterceptor(@LoginUser User user) {
        // LoginCheckInterceptor 를 통과하지 않은 Controller
        return new ResponseEntity<>(HttpStatus.OK);
    }

    //
    @PostMapping("/reissue")
    public ResponseEntity<UserLoginResponseDto> reissueToken(@RequestBody @Valid UserTokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(userService.reissueToken(tokenRequestDto), HttpStatus.OK);
    }

    @GetMapping("/qr-name")
    public ResponseEntity<UserQrAndNameResponseDto> userQrAndNameInfo(@LoginUser User user) {
        return new ResponseEntity<>(userService.userQrAndName(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/admin/check")
    public ResponseEntity<UserForAdminResponseDto> adminCheckUserInfo(@LoginUser User user, @RequestParam String userLoginId) {
        //todo : 서비스로 넣지 말고 security 적용 후 메서드 접근 권한 설정
        if (user.getRole() != UserRoleType.ADMIN) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userService.AdminCheckUserInfo(userLoginId), HttpStatus.OK);
    }

    @GetMapping("/admin/check/user-id")
    public ResponseEntity<Void> adminCheckUserLoginId(@RequestParam String userLoginId) {
        userService.checkLoginId(userLoginId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

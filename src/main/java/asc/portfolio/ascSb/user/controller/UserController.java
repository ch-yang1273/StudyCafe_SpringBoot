package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.user.dto.*;
import asc.portfolio.ascSb.user.exception.AccessDeniedException;
import asc.portfolio.ascSb.user.exception.TokenException;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
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
    public ResponseEntity<String> tokenExHandle(TokenException ex) {
        return new ResponseEntity<>("TokenException", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UnknownUserException.class)
    public ResponseEntity<String> unknownExHandle(UnknownUserException ex) {
        return new ResponseEntity<>("UnknownUserException", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> accessDeniedExHandle(AccessDeniedException ex) {
        return new ResponseEntity<>("AccessDeniedException", HttpStatus.BAD_REQUEST);
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
        log.debug("login user = {}", user.getLoginId());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<UserLoginResponseDto> reissueToken(@RequestBody @Valid UserTokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(
                userService.reissueToken(tokenRequestDto.getAccessToken(), tokenRequestDto.getRefreshToken()),
                HttpStatus.OK);
    }

    @GetMapping("/qr-name")
    public ResponseEntity<UserQrAndNameResponseDto> userQrAndNameInfo(@LoginUser User user) {
        return new ResponseEntity<>(userService.userQrAndName(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/admin/check")
    public ResponseEntity<UserProfileDto> getUserInfo(@LoginUser User user, @RequestParam String userLoginId) {
        userService.checkAdminRole(user.getId());
        return new ResponseEntity<>(userService.getUserInfo(userLoginId), HttpStatus.OK);
    }

    @GetMapping("/admin/check/user-id")
    public ResponseEntity<Void> checkUserInfo(@RequestParam String userLoginId) {
        userService.getUserInfo(userLoginId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

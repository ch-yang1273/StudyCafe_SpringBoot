package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.user.dto.UserLoginRequestDto;
import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import asc.portfolio.ascSb.user.dto.UserProfileDto;
import asc.portfolio.ascSb.user.dto.UserQrAndNameResponseDto;
import asc.portfolio.ascSb.user.dto.UserSignupDto;
import asc.portfolio.ascSb.user.dto.UserTokenRequestDto;
import asc.portfolio.ascSb.user.exception.AccessDeniedException;
import asc.portfolio.ascSb.user.exception.TokenException;
import asc.portfolio.ascSb.user.exception.UserNotFoundException;
import asc.portfolio.ascSb.user.service.UserAuthService;
import asc.portfolio.ascSb.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
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

    private final UserAuthService userAuthService;
    private final UserService userService;

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> tokenExHandle(TokenException ex) {
        return new ResponseEntity<>("TokenException", HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> unknownExHandle(UserNotFoundException ex) {
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
            userAuthService.signUp(signUpDto);
        } catch (DataIntegrityViolationException ex) {
            return new ResponseEntity<>("Unique violation", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDto> login(@RequestBody @Valid UserLoginRequestDto loginDto) {
        UserLoginResponseDto loginRespDto = userAuthService.checkPassword(loginDto.getLoginId(), loginDto.getPassword());
        return new ResponseEntity<>(loginRespDto, HttpStatus.OK);
    }

    @GetMapping("/login-check") //Test
    public ResponseEntity<Void> loginCheck(@LoginUser Long userId) {
        log.debug("login user = {}", userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<UserLoginResponseDto> reissueToken(@RequestBody @Valid UserTokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(
                userAuthService.reissueToken(tokenRequestDto.getAccessToken(), tokenRequestDto.getRefreshToken()),
                HttpStatus.OK);
    }

    @GetMapping("/qr-name")
    public ResponseEntity<UserQrAndNameResponseDto> userQrAndNameInfo(@LoginUser Long userId) {
        return new ResponseEntity<>(userService.userQrAndName(userId), HttpStatus.OK);
    }

    @GetMapping("/admin/check")
    public ResponseEntity<UserProfileDto> getUserInfoByLoginId(@LoginUser Long adminId, @RequestParam String userLoginId) {
        userAuthService.checkAdminRole(adminId);
        return new ResponseEntity<>(userService.getUserInfoByLoginId(userLoginId), HttpStatus.OK);
    }

    @GetMapping("/admin/check/user-id")
    public ResponseEntity<Void> checkUserInfoByLoginId(@RequestParam String userLoginId) {
        userService.getUserInfoByLoginId(userLoginId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Parameter(name = "cafeId", example = "1")
    @PostMapping("/update/{cafeId}")
    public ResponseEntity<Void> updateUserCafe(@LoginUser Long userId, @PathVariable Long cafeId) {
        userAuthService.checkUserRole(userId);
        userService.updateUserCafe(userId, cafeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

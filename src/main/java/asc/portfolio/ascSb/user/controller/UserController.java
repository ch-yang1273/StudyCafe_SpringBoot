package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.user.dto.UserLoginRequest;
import asc.portfolio.ascSb.user.dto.UserLoginResponse;
import asc.portfolio.ascSb.user.dto.UserProfile;
import asc.portfolio.ascSb.user.dto.UserQrCodeResponse;
import asc.portfolio.ascSb.user.dto.UserSignupRequest;
import asc.portfolio.ascSb.user.dto.UserReissueRequest;
import asc.portfolio.ascSb.user.service.UserAuthService;
import asc.portfolio.ascSb.user.service.UserRoleCheckService;
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

    private final UserAuthService userAuthService;
    private final UserRoleCheckService userRoleCheckService;
    private final UserService userService;

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
    public ResponseEntity<String> singUp(@RequestBody @Valid UserSignupRequest signUpDto, BindingResult bindingResult) {
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
    public ResponseEntity<UserLoginResponse> login(@RequestBody @Valid UserLoginRequest loginDto) {
        UserLoginResponse loginRespDto = userAuthService.checkPassword(loginDto.getLoginId(), loginDto.getPassword());
        return new ResponseEntity<>(loginRespDto, HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity<UserLoginResponse> reissueToken(@RequestBody @Valid UserReissueRequest tokenRequestDto) {
        return new ResponseEntity<>(
                userAuthService.reissueToken(tokenRequestDto.getAccessToken(), tokenRequestDto.getRefreshToken()),
                HttpStatus.OK);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getMyProfile(@LoginUser Long userId) {
        return ResponseEntity.ok().body(userService.getProfileById(userId));
    }

    @GetMapping("/qr")
    public ResponseEntity<UserQrCodeResponse> getQrCode(@LoginUser Long userId) {
        return new ResponseEntity<>(userService.userQrAndName(userId), HttpStatus.OK);
    }

    @GetMapping("/admin/check")
    public ResponseEntity<UserProfile> getUserInfoByLoginId(@LoginUser Long adminId, @RequestParam String userLoginId) {
        userRoleCheckService.isAdmin(adminId);
        return new ResponseEntity<>(userService.getUserInfoByLoginId(userLoginId), HttpStatus.OK);
    }

    @GetMapping("/admin/check/user-id")
    public ResponseEntity<Void> checkUserInfoByLoginId(@RequestParam String userLoginId) {
        userService.getUserInfoByLoginId(userLoginId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

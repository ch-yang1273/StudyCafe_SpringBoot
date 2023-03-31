package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.user.dto.UserLoginRequest;
import asc.portfolio.ascSb.user.domain.TokenPairDto;
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

    //bindingResult 관련도 에러 메시지 통합할 수 있겠다. 발생되는 에러 유형만 확인해서 Advice 만듭시다.
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

        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/login")
    public ResponseEntity<TokenPairDto> login(@RequestBody @Valid UserLoginRequest loginDto) {
        TokenPairDto loginRespDto = userAuthService.checkPassword(loginDto.getLoginId(), loginDto.getPassword());
        return ResponseEntity.ok().body(loginRespDto);
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenPairDto> reissueToken(@RequestBody @Valid UserReissueRequest tokenRequestDto) {
        return ResponseEntity.ok()
                .body(userAuthService.reissueToken(
                        tokenRequestDto.getAccessToken(),
                        tokenRequestDto.getRefreshToken()));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfile> getMyProfile(@LoginUser Long userId) {
        return ResponseEntity.ok().body(userService.getProfileById(userId));
    }

    @GetMapping("/qr")
    public ResponseEntity<UserQrCodeResponse> getQrCode(@LoginUser Long userId) {
        return ResponseEntity.ok().body(userService.userQrAndName(userId));
    }

    @GetMapping("/admin/check")
    public ResponseEntity<UserProfile> getUserInfoByLoginId(@LoginUser Long adminId, @RequestParam String userLoginId) {
        userRoleCheckService.isAdmin(adminId);
        return ResponseEntity.ok().body(userService.getUserInfoByLoginId(userLoginId));
    }

    @GetMapping("/admin/check/user-id")
    public ResponseEntity<Void> checkUserInfoByLoginId(@RequestParam String userLoginId) {
        userService.getUserInfoByLoginId(userLoginId);
        return ResponseEntity.ok().build();
    }
}

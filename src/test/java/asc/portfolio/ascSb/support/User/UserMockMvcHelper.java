package asc.portfolio.ascSb.support.User;

import asc.portfolio.ascSb.user.dto.UserLoginRequestDto;
import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import asc.portfolio.ascSb.user.dto.UserSignupDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SuppressWarnings("NonAsciiCharacters")
public class UserMockMvcHelper {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper;

    static final String SIGNUP_URL = "/api/v1/user/signup";
    static final String LOGIN_URL = "/api/v1/user/login";
//    static final String LOGIN_CHECK_URL = "/api/v1/user/login-check";
//    static final String REISSUE_TOKEN_URL = "/api/v1/user/reissue";
//    static final String QR_REQ_URL = "/api/v1/user/qr-name";
//    static final String USER_INFO_URL = "/api/v1/user//admin/check";

    public UserMockMvcHelper(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
        this.mapper = new ObjectMapper();
    }

    private String fromDtoToJson(Object dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultActions 회원가입을_한다(UserFixture fixture) throws Exception {
        UserSignupDto dto = UserSignupDto.builder()
                .loginId(fixture.getLoginId())
                .password(fixture.getPassword())
                .email(fixture.getEmail())
                .build();

        String content = fromDtoToJson(dto);
        return mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }

    public ResultActions 로그인을_한다(UserFixture fixture) throws Exception {
        UserLoginRequestDto dto = UserLoginRequestDto.builder()
                .loginId(fixture.getLoginId())
                .password(fixture.getPassword())
                .build();

        String content = fromDtoToJson(dto);
        return mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }

    public ResultActions 회원가입과_로그인을_한다(UserFixture fixture) throws Exception {
        회원가입을_한다(fixture);
        return 로그인을_한다(fixture);
    }

    public UserLoginResponseDto 액세스토큰을_받는다(UserFixture fixture) throws Exception {
        String contentAsString = 로그인을_한다(fixture)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(contentAsString, UserLoginResponseDto.class);
    }

    public UserLoginResponseDto 회원가입_후_액세스토큰을_받는다(UserFixture fixture) throws Exception {
        회원가입을_한다(fixture);
        return 액세스토큰을_받는다(fixture);
    }
}

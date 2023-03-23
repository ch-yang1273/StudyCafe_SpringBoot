package asc.portfolio.ascSb.support.User;

import asc.portfolio.ascSb.user.dto.UserLoginRequestDto;
import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import asc.portfolio.ascSb.user.dto.UserProfileDto;
import asc.portfolio.ascSb.user.dto.UserSignupDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SuppressWarnings("NonAsciiCharacters")
@RequiredArgsConstructor
public class UserMockMvcHelper {

    private final MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    static final String BASE_URL = "/api/v1/user";
    static final String SIGNUP_URL = BASE_URL + "/signup";
    static final String LOGIN_URL = BASE_URL + "/login";
    static final String PROFILE_URL = BASE_URL + "/profile";

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

    public ResultActions 내_Profile을_조회한다(String accessToken) throws Exception {

        return mockMvc.perform(MockMvcRequestBuilders.get(PROFILE_URL)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions 내_Profile을_조회한다(UserFixture fixture) throws Exception {
        UserLoginResponseDto dto = 액세스토큰을_받는다(fixture);
        String accessToken = dto.getAccessToken();

        return 내_Profile을_조회한다(accessToken);
    }

    public UserProfileDto 내_Profile을_받는다(UserFixture fixture) throws Exception {
        String contentAsString = 내_Profile을_조회한다(fixture)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(contentAsString, UserProfileDto.class);
    }

    public UserProfileDto 회원가입_후_내_Profile을_받는다(UserFixture fixture) throws Exception {
        회원가입을_한다(fixture);
        return 내_Profile을_받는다(fixture);
    }
}

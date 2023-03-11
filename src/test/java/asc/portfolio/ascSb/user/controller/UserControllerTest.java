package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.dto.UserLoginRequestDto;
import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import asc.portfolio.ascSb.user.dto.UserSignupDto;
import asc.portfolio.ascSb.user.dto.UserTokenRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    static final String SIGNUP_URL = "/api/v1/user/signup";
    static final String LOGIN_URL = "/api/v1/user/login";
    static final String LOGIN_CHECK_URL = "/api/v1/user/login-check";
    static final String REISSUE_TOKEN_URL = "/api/v1/user/reissue";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private CurrentTimeProvider currentTimeProvider;

    private UserSignupDto createTestUserSignupDto(String prefix) {
        return UserSignupDto.builder()
                .loginId(prefix + "testUSer")
                .password(prefix + "password")
                .email(prefix + "email@gmail.com")
                .name(prefix + "test")
                .build();
    }

    private UserSignupDto createTestUserSignupDto() {
        return createTestUserSignupDto("");
    }

    private String fromDtoToJson(Object dto) {
        try {
            return new ObjectMapper().writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Test
    @DisplayName("올바른 SingUp 등록에는 200을 반환하고, password는 암호화 된다.")
    public void User_등록() throws Exception {

        //given
        UserSignupDto requestDto = createTestUserSignupDto();

        String jsonContent = fromDtoToJson(requestDto);
        System.out.println("content = " + jsonContent);
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        User findUser = userRepository.findByLoginId(requestDto.getLoginId()).orElseThrow();

        assertThat(findUser.getPassword()).isNotEqualTo(requestDto.getPassword());
        assertThat(findUser.getEmail()).isEqualTo(requestDto.getEmail());
    }

    @Transactional
    @Test
    @DisplayName("중복등록에는 BadRequest status를 반환한다.")
    public void User_중복등록() throws Exception {
        //given
        UserSignupDto requestDto = createTestUserSignupDto();
        String jsonContent = fromDtoToJson(requestDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    public void User_패스워드_UppercaseStart() throws Exception {
        //given
        UserSignupDto requestDto = createTestUserSignupDto("U");
        String jsonContent = fromDtoToJson(requestDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    public void User_패스워드_LowercaseStart() throws Exception {
        //given
        UserSignupDto requestDto = createTestUserSignupDto("l");
        String jsonContent = fromDtoToJson(requestDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("기본 login 성공 테스트")
    public void User_Login() throws Exception {
        //given
        UserSignupDto requestSignupDto = createTestUserSignupDto();
        UserLoginRequestDto requestLoginDto = UserLoginRequestDto.builder()
                .loginId(requestSignupDto.getLoginId())
                .password(requestSignupDto.getPassword())
                .build();

        String signupContent = fromDtoToJson(requestSignupDto);
        String loginContent = fromDtoToJson(requestLoginDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Transactional
    @Test
    @DisplayName("존재하지 않는 loginId로 로그인 시에는 Bad Request를 반환한다.")
    public void 존재하지_않는_loginId로_로그인() throws Exception {
        //given
        UserSignupDto requestSignupDto = createTestUserSignupDto();
        UserLoginRequestDto requestLoginDto = UserLoginRequestDto.builder()
                .loginId("f" + requestSignupDto.getLoginId())
                .password(requestSignupDto.getPassword())
                .build();

        String signupContent = fromDtoToJson(requestSignupDto);
        String loginContent = fromDtoToJson(requestLoginDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("아이디를 넣지 않았을 때는 Bad Request를 반환한다.")
    public void 아이디를_넣지_않았을_때() throws Exception {
        //given
        UserSignupDto requestSignupDto = createTestUserSignupDto();
        UserLoginRequestDto requestLoginDto = UserLoginRequestDto.builder()
                .password(requestSignupDto.getPassword())
                .build();

        String signupContent = fromDtoToJson(requestSignupDto);
        String loginContent = fromDtoToJson(requestLoginDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("비밀번호를 넣지 않았을 때에는 Bad Request를 반환한다.")
    public void 비밀번호를_넣지_않았을_때() throws Exception {
        //given
        UserSignupDto requestSignupDto = createTestUserSignupDto();
        UserLoginRequestDto requestLoginDto = UserLoginRequestDto.builder()
                .loginId(requestSignupDto.getLoginId())
                .build();

        String signupContent = fromDtoToJson(requestSignupDto);
        String loginContent = fromDtoToJson(requestLoginDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("비밀번호를 틀렸을 때는 Bad Request를 반환한다.")
    public void 비밀번호를_틀렸을_때() throws Exception {
        //given
        UserSignupDto requestSignupDto = createTestUserSignupDto();
        UserLoginRequestDto requestLoginDto = UserLoginRequestDto.builder()
                .loginId(requestSignupDto.getLoginId())
                .password("f" + requestSignupDto.getPassword())
                .build();

        String signupContent = fromDtoToJson(requestSignupDto);
        String loginContent = fromDtoToJson(requestLoginDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("accessToken을 갖고 접근했을 때 OK 반환")
    public void accessToken을_갖고_접근() throws Exception {
        //given
        UserSignupDto requestSignupDto = createTestUserSignupDto();
        UserLoginRequestDto requestLoginDto = UserLoginRequestDto.builder()
                .loginId(requestSignupDto.getLoginId())
                .password(requestSignupDto.getPassword())
                .build();

        String signupContent = fromDtoToJson(requestSignupDto);
        String loginContent = fromDtoToJson(requestLoginDto);

        //when //then
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respJson = mvcResult.getResponse().getContentAsString();
        UserLoginResponseDto dto = new ObjectMapper().readValue(respJson, UserLoginResponseDto.class);

        String accessToken = dto.getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get(LOGIN_CHECK_URL)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("accessToken 없이 갖고 접근했을 때 Unauthorized 반환")
    public void accessToken_없이_접근() throws Exception {
        //given
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get(LOGIN_CHECK_URL))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("올바르지 않은 accessToken으로 접근했을 때 Unauthorized 반환")
    public void 올바르지_않은_accessToken으로_접근() throws Exception {
        //given
        //when //then
        mockMvc.perform(MockMvcRequestBuilders.get(LOGIN_CHECK_URL)
                        .header(HttpHeaders.AUTHORIZATION, "test"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Transactional
    @Test
    @DisplayName("refreshToken으로 accessToken 갱신 성공")
    public void refreshToken으로_asscessToken_갱신() throws Exception {
        //given
        UserSignupDto requestSignupDto = createTestUserSignupDto();
        UserLoginRequestDto requestLoginDto = UserLoginRequestDto.builder()
                .loginId(requestSignupDto.getLoginId())
                .password(requestSignupDto.getPassword())
                .build();

        String signupContent = fromDtoToJson(requestSignupDto);
        String loginContent = fromDtoToJson(requestLoginDto);

        //when
        mockMvc.perform(MockMvcRequestBuilders.post(SIGNUP_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(signupContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respJson = mvcResult.getResponse().getContentAsString();
        UserLoginResponseDto loginDto = new ObjectMapper().readValue(respJson, UserLoginResponseDto.class);

        UserTokenRequestDto tokenRequestDto = new UserTokenRequestDto();
        tokenRequestDto.setAccessToken(loginDto.getAccessToken());
        tokenRequestDto.setRefreshToken(loginDto.getRefreshToken());

        String tokenReqContent = fromDtoToJson(tokenRequestDto);

        // 현재 시간을 반환하는 로직이 1분뒤의 시간을 반환하도록 Mocking
        BDDMockito.given(currentTimeProvider.now()).willReturn(LocalDateTime.now().plusMinutes(1L));
        //then
        mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(REISSUE_TOKEN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tokenReqContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        respJson =  mvcResult.getResponse().getContentAsString();
        UserLoginResponseDto reissueDto = new ObjectMapper().readValue(respJson, UserLoginResponseDto.class);

        Assertions.assertThat(loginDto.getAccessToken()).isNotEqualTo(reissueDto.getAccessToken());
        Assertions.assertThat(loginDto.getRefreshToken()).isEqualTo(reissueDto.getRefreshToken());
    }
}

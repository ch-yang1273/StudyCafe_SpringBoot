package asc.portfolio.ascSb.user.controller;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRepository;
import asc.portfolio.ascSb.user.dto.UserLoginRequestDto;
import asc.portfolio.ascSb.user.dto.UserLoginResponseDto;
import asc.portfolio.ascSb.user.dto.UserSignupDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    static final String SIGNUP_URL = "/api/v1/user/signup";
    static final String LOGIN_URL = "/api/v1/user/login";
    static final String LOGIN_CHECK_URL = "/api/v1/user/login-check";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private UserSignupDto createTestUserSignupDto(String prefix) {
        return UserSignupDto.builder()
                .loginId(prefix + "testUSer")
                .password(prefix + "password")
                .email(prefix + "email@gmail.com")
                .name(prefix + "test")
                .build();
    }

    private UserSignupDto createTestUserSignupDto() {
        return createTestUserSignupDto(null);
    }

    private String fromDtoToJson(Object dto) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(dto);
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
}

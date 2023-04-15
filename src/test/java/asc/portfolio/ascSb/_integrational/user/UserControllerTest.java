package asc.portfolio.ascSb._integrational.user;

import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.User.UserMockMvcHelper;
import asc.portfolio.ascSb.support.User.UserRepositoryHelper;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.dto.UserLoginRequest;
import asc.portfolio.ascSb.user.domain.TokenPairDto;
import asc.portfolio.ascSb.user.dto.UserQrCodeResponse;
import asc.portfolio.ascSb.user.dto.UserReissueRequest;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@Import({UserMockMvcHelper.class, UserRepositoryHelper.class})
class UserControllerTest {

    static final String BASE_URL = "/api/v1/user";
    static final String LOGIN_URL = BASE_URL + "/login";
    static final String REISSUE_TOKEN_URL = BASE_URL + "/reissue";
    static final String QR_REQ_URL = BASE_URL + "/qr";
    static final String USER_INFO_URL = BASE_URL + "/admin/check";
    static final String PROFILE_URL = BASE_URL + "/profile";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserMockMvcHelper mvcHelper;

    @Autowired
    private UserRepositoryHelper repositoryHelper;

    @SpyBean
    private CurrentTimeProvider currentTimeProvider;

    private String fromDtoToJson(Object dto) {
        try {
            return new ObjectMapper().writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("올바른 SingUp 등록에는 200을 반환하고, password는 암호화 된다.")
    public void User_회원가입() throws Exception {

        //given
        UserFixture bloo = UserFixture.BLOO;
        mvcHelper.회원가입을_한다(bloo)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        //when
        User findUser = repositoryHelper.유저를_찾는다(bloo);

        //then
        assertThat(findUser.getPassword()).isNotEqualTo(bloo.getPassword());
        assertThat(findUser.getEmail()).isEqualTo(bloo.getEmail());
    }

    @Test
    @DisplayName("중복 회원가입에는 Confilct status를 반환한다.")
    public void User_중복_회원가입() throws Exception {
        mvcHelper.회원가입을_한다(UserFixture.BLOO)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        mvcHelper.회원가입을_한다(UserFixture.BLOO)
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("기본 login 성공 테스트")
    public void User_Login() throws Exception {
        mvcHelper.회원가입과_로그인을_한다(UserFixture.BLOO)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("존재하지 않는 loginId로 로그인 시에는 Bad Request를 반환한다.")
    public void 존재하지_않는_loginId로_로그인() throws Exception {
        mvcHelper.로그인을_한다(UserFixture.BLOO)
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("loginId를 넣지 않았을 때는 Bad Request를 반환한다.")
    public void loginId를_넣지_않았을_때() throws Exception {
        mvcHelper.회원가입을_한다(UserFixture.BLOO);
        UserLoginRequest requestLoginDto = UserLoginRequest.builder()
                .password(UserFixture.BLOO.getPassword())
                .build();

        String loginContent = fromDtoToJson(requestLoginDto);

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("비밀번호를 넣지 않았을 때에는 Bad Request를 반환한다.")
    public void 비밀번호를_넣지_않았을_때() throws Exception {
        mvcHelper.회원가입을_한다(UserFixture.BLOO);

        UserLoginRequest requestLoginDto = UserLoginRequest.builder()
                .loginId(UserFixture.BLOO.getLoginId())
                .build();

        String loginContent = fromDtoToJson(requestLoginDto);

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("비밀번호를 틀렸을 때는 Bad Request를 반환한다.")
    public void 비밀번호를_틀렸을_때() throws Exception {
        mvcHelper.회원가입을_한다(UserFixture.BLOO);

        UserLoginRequest requestLoginDto = UserLoginRequest.builder()
                .loginId(UserFixture.BLOO.getLoginId())
                .password("f" + UserFixture.BLOO.getPassword())
                .build();

        String loginContent = fromDtoToJson(requestLoginDto);

        mockMvc.perform(MockMvcRequestBuilders.post(LOGIN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginContent))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("accessToken을 갖고 접근했을 때 OK 반환")
    public void accessToken을_갖고_접근() throws Exception {
        TokenPairDto dto = mvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);
        mvcHelper.내_Profile을_조회한다(dto.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("accessToken 없이 갖고 접근했을 때 Unauthorized 반환")
    public void accessToken_없이_접근() throws Exception {
        TokenPairDto dto = mvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);

        mockMvc.perform(MockMvcRequestBuilders.get(PROFILE_URL)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("올바르지 않은 accessToken으로 접근했을 때 Unauthorized 반환")
    public void 올바르지_않은_accessToken으로_접근() throws Exception {
        TokenPairDto dto = mvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);
        mvcHelper.내_Profile을_조회한다("failTest")
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("refreshToken으로 accessToken 갱신 성공")
    public void refreshToken으로_accessToken_갱신() throws Exception {
        TokenPairDto loginDto = mvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);

        UserReissueRequest tokenRequestDto = new UserReissueRequest();
        tokenRequestDto.setAccessToken(loginDto.getAccessToken());
        tokenRequestDto.setRefreshToken(loginDto.getRefreshToken());

        String tokenReqContent = fromDtoToJson(tokenRequestDto);

        // 현재 시간을 반환하는 로직이 1분뒤의 시간을 반환하도록 Mocking
        BDDMockito.given(currentTimeProvider.localDateTimeNow()).willReturn(LocalDateTime.now().plusMinutes(1L));
        //then
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(REISSUE_TOKEN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tokenReqContent))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String respJson = mvcResult.getResponse().getContentAsString();
        TokenPairDto reissueDto = new ObjectMapper().readValue(respJson, TokenPairDto.class);

        Assertions.assertThat(loginDto.getAccessToken()).isNotEqualTo(reissueDto.getAccessToken());
        Assertions.assertThat(loginDto.getRefreshToken()).isEqualTo(reissueDto.getRefreshToken());
    }

    @Test
    @DisplayName("올바르지 않은 refreshToken으로 accessToken 갱신 실패")
    public void refreshToken으로_accessToken_갱신실패() throws Exception {
        TokenPairDto loginDto = mvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);
        UserReissueRequest tokenRequestDto = new UserReissueRequest();
        tokenRequestDto.setAccessToken(loginDto.getAccessToken());
        tokenRequestDto.setRefreshToken("Fail");

        String tokenReqContent = fromDtoToJson(tokenRequestDto);

        mockMvc.perform(MockMvcRequestBuilders.post(REISSUE_TOKEN_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tokenReqContent))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }


    @Test
    @DisplayName("QrCode를 요청 시 QrCode 반환. OK")
    public void qrCode_요청() throws Exception {
        //given
        TokenPairDto dto = mvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);

        String accessToken = dto.getAccessToken();

        MvcResult qrResult = mockMvc.perform(MockMvcRequestBuilders.get(QR_REQ_URL)
                        .header(HttpHeaders.AUTHORIZATION, accessToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        String qrJson = qrResult.getResponse().getContentAsString();
        UserQrCodeResponse qrDto = new ObjectMapper().readValue(qrJson, UserQrCodeResponse.class);
        assertThat(qrDto.getQrCode()).isNotBlank();
    }

    @Test
    @DisplayName("Admin 권한의 회원 조회 성공")
    public void 회원조회_byAdmin() throws Exception {
        UserFixture adminFixture = UserFixture.ADMIN_BUTTERCUP;
        repositoryHelper.유저를_등록한다(adminFixture);
        TokenPairDto dto = mvcHelper.액세스토큰을_받는다(adminFixture);

        String accessToken = dto.getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get(USER_INFO_URL)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .param("userLoginId", adminFixture.getLoginId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @DisplayName("USER 권한의 회원 조회 실패")
    public void 회원조회_byUser() throws Exception {
        TokenPairDto dto = mvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);

        String accessToken = dto.getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get(USER_INFO_URL)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .param("userLoginId", UserFixture.BLOO.getLoginId()))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }

    @Test
    @DisplayName("Admin 권한의 없는 회원 조회")
    public void 없는_회원조회_byAdmin() throws Exception {
        repositoryHelper.유저를_등록한다(UserFixture.ADMIN_BLOSSOM);
        TokenPairDto dto = mvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);

        String accessToken = dto.getAccessToken();

        mockMvc.perform(MockMvcRequestBuilders.get(USER_INFO_URL)
                        .header(HttpHeaders.AUTHORIZATION, accessToken)
                        .param("userLoginId", "toFail"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
    }
}

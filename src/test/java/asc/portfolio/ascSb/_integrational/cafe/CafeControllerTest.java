package asc.portfolio.ascSb._integrational.cafe;

import asc.portfolio.ascSb.cafe.dto.CafeResponse;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.User.UserMockMvcHelper;
import asc.portfolio.ascSb.support.User.UserRepositoryHelper;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
import asc.portfolio.ascSb.support.cafe.CafeMockMvcHelper;
import asc.portfolio.ascSb.user.domain.TokenPairDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@Import({CafeMockMvcHelper.class, UserMockMvcHelper.class, UserRepositoryHelper.class})
class CafeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CafeMockMvcHelper cafeMockMvcHelper;

    @Autowired
    private UserMockMvcHelper userMockMvcHelper;

    @Autowired
    private UserRepositoryHelper userRepositoryHelper;

    @BeforeEach
    public void setAdmin() throws Exception {
        userRepositoryHelper.유저를_등록한다(UserFixture.ADMIN_BLOSSOM);
        userRepositoryHelper.유저를_등록한다(UserFixture.ADMIN_BUTTERCUP);
    }

    @Test
    @DisplayName("admin 계정으로 카페 등록")
    public void 카페를_등록한다() throws Exception {
        TokenPairDto dto = userMockMvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);
        cafeMockMvcHelper.카페를_등록한다(CafeFixture.CAFE_A, dto.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("user 계정으로 카페 등록 실패")
    public void 카페를_등록한다_byUser() throws Exception {
        TokenPairDto dto = userMockMvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);
        cafeMockMvcHelper.카페를_등록한다(CafeFixture.CAFE_A, dto.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @DisplayName("등록한 카페를 조회한다")
    public void 카페를_조회한다() throws Exception {
        TokenPairDto dto = userMockMvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);
        cafeMockMvcHelper.카페를_등록한다(CafeFixture.CAFE_A, dto.getAccessToken());
        cafeMockMvcHelper.카페를_조회한다(dto.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("등록한 카페를 Open 한다")
    public void 카페를_Open한다() throws Exception {
        TokenPairDto blossom = userMockMvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);
        CafeResponse info = cafeMockMvcHelper.카페를_등록후_조회정보를_받는다(CafeFixture.CAFE_A, blossom.getAccessToken());

        cafeMockMvcHelper.카페를_Open한다(info.getCafeId(), blossom.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("등록한 카페를 Close 한다")
    public void 카페를_Close한다() throws Exception {
        TokenPairDto blossom = userMockMvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);
        CafeResponse info = cafeMockMvcHelper.카페를_등록후_조회정보를_받는다(CafeFixture.CAFE_A, blossom.getAccessToken());

        cafeMockMvcHelper.카페를_Close한다(info.getCafeId(), blossom.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
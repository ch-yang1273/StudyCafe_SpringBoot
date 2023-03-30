package asc.portfolio.ascSb.follow.controller;

import asc.portfolio.ascSb.cafe.dto.CafeResponse;
import asc.portfolio.ascSb.follow.dto.CafeFollowersResponse;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.User.UserMockMvcHelper;
import asc.portfolio.ascSb.support.User.UserRepositoryHelper;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
import asc.portfolio.ascSb.support.cafe.CafeMockMvcHelper;
import asc.portfolio.ascSb.support.follw.FollowMockMvcHelper;
import asc.portfolio.ascSb.user.dto.UserLoginResponse;
import asc.portfolio.ascSb.user.infra.MapTokenRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureMockMvc
@Transactional
@SpringBootTest
@Import({
        MapTokenRepository.class, CafeMockMvcHelper.class,
        UserMockMvcHelper.class, UserRepositoryHelper.class,
        FollowMockMvcHelper.class
})
class FollowControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CafeMockMvcHelper cafeMockMvcHelper;

    @Autowired
    private UserMockMvcHelper userMockMvcHelper;

    @Autowired
    private UserRepositoryHelper userRepositoryHelper;

    @Autowired
    private FollowMockMvcHelper followMockMvcHelper;

    @BeforeEach
    public void setAdmin() throws Exception {
        userRepositoryHelper.유저를_등록한다(UserFixture.ADMIN_BLOSSOM);
        userRepositoryHelper.유저를_등록한다(UserFixture.ADMIN_BUTTERCUP);
    }

    @Test
    @DisplayName("카페를 Unfollow 한다")
    public void 카페를_Unfollow한다() throws Exception {
        UserLoginResponse blossom = userMockMvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);
        CafeResponse info = cafeMockMvcHelper.카페를_등록후_조회정보를_받는다(CafeFixture.CAFE_A, blossom.getAccessToken());

        UserLoginResponse dto = userMockMvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.DAISY);
        followMockMvcHelper.카페를_Unfollow한다(info.getCafeId(), dto.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("카페를 Follow 한다")
    public void 카페를_Follow한다() throws Exception {
        UserLoginResponse blossom = userMockMvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);
        CafeResponse info = cafeMockMvcHelper.카페를_등록후_조회정보를_받는다(CafeFixture.CAFE_A, blossom.getAccessToken());

        UserLoginResponse dto = userMockMvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.DAISY);
        followMockMvcHelper.카페를_Follow한다(info.getCafeId(), dto.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("카페 follower 정보를 받는다")
    public void 카페_Follower_정보를_받는다() throws Exception {
        UserLoginResponse blossom = userMockMvcHelper.액세스토큰을_받는다(UserFixture.ADMIN_BLOSSOM);
        CafeResponse info = cafeMockMvcHelper.카페를_등록후_조회정보를_받는다(CafeFixture.CAFE_A, blossom.getAccessToken());

        UserLoginResponse dto1 = userMockMvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.BLOO);
        UserLoginResponse dto2 = userMockMvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.COCO);
        UserLoginResponse dto3 = userMockMvcHelper.회원가입_후_액세스토큰을_받는다(UserFixture.DAISY);
        followMockMvcHelper.카페를_Follow한다(info.getCafeId(), dto1.getAccessToken());
        followMockMvcHelper.카페를_Follow한다(info.getCafeId(), dto2.getAccessToken());
        followMockMvcHelper.카페를_Follow한다(info.getCafeId(), dto3.getAccessToken());

        String response = followMockMvcHelper.카페_Follower_정보를_받는다(info.getCafeId(), blossom.getAccessToken())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        CafeFollowersResponse followers = new ObjectMapper().readValue(response, CafeFollowersResponse.class);

        assertThat(followers.getCafeId()).isEqualTo(info.getCafeId());
        assertThat(followers.getCount()).isEqualTo(3);
    }
}
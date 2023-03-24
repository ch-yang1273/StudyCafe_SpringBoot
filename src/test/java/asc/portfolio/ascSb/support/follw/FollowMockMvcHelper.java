package asc.portfolio.ascSb.support.follw;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@Slf4j
@SuppressWarnings("NonAsciiCharacters")
@RequiredArgsConstructor
public class FollowMockMvcHelper {

    private final MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private final String BASE_URL = "/api/v1/follow";
    private final String FOLLOW = "/following";
    private final String UNFOLLOW = "/unfollowing";
    private final String FOLLOWERS = "/followers";

    private String getUrl(Long cafeId, String suffix) {
        //BASE_URL/{cafeId}/suffix
        return BASE_URL + "/" + cafeId + suffix;
    }

    public ResultActions 카페를_Follow한다(Long cafeId, String accessToken) throws Exception {
        String url = getUrl(cafeId, FOLLOW);

        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions 카페를_Unfollow한다(Long cafeId, String accessToken) throws Exception {
        String url = getUrl(cafeId, UNFOLLOW);

        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions 카페_Follower_정보를_받는다(Long cafeId, String accessToken) throws Exception {
        String url = getUrl(cafeId, FOLLOWERS);

        return mockMvc.perform(MockMvcRequestBuilders.get(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON));
    }
}

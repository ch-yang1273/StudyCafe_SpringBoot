package asc.portfolio.ascSb.support.cafe;

import asc.portfolio.ascSb.cafe.dto.CafeRegistrationRequest;
import asc.portfolio.ascSb.cafe.dto.CafeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class CafeMockMvcHelper {

    private final MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    private final String BASE_URL = "/api/v1/cafe";
    private final String CAFE_LIST_URL = BASE_URL + "/list";
    private final String REGISTER_URL = BASE_URL + "/register";
    private final String INFO_URL = BASE_URL + "/info";

    private final String OPEN = "/open";
    private final String CLOSE = "/close";
    private final String SEATS = "/seats";
    private final String FOLLOW = "/follow";
    private final String UNFOLLOW = "/unfollow";
    private final String FOLLOWERS = "/followers";

    private String getUrl(Long cafeId, String suffix) {
        //BASE_URL/{cafeId}/suffix
        return BASE_URL + "/" + cafeId + suffix;
    }

    private String fromDtoToJson(Object dto) {
        try {
            return mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultActions 카페를_등록한다(CafeFixture fixture, String accessToken) throws Exception {
        CafeRegistrationRequest dto = new CafeRegistrationRequest(fixture.getCafeName(), fixture.getCafeArea());
        String content = fromDtoToJson(dto);
        return mockMvc.perform(MockMvcRequestBuilders.post(REGISTER_URL)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content));
    }

    public ResultActions 카페를_조회한다(String accessToken) throws Exception {
        return mockMvc.perform(MockMvcRequestBuilders.get(INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public CafeResponse 카페_조회정보를_받는다(String accessToken) throws Exception {
        String contentAsString = 카페를_조회한다(accessToken)
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(contentAsString, CafeResponse.class);
    }

    public CafeResponse 카페를_등록후_조회정보를_받는다(CafeFixture fixture, String accessToken) throws Exception {
        카페를_등록한다(fixture, accessToken);
        return 카페_조회정보를_받는다(accessToken);
    }

    public ResultActions 카페를_Open한다(Long cafeId, String accessToken) throws Exception {
        String url = getUrl(cafeId, OPEN);

        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON));
    }

    public ResultActions 카페를_Close한다(Long cafeId, String accessToken) throws Exception {
        String url = getUrl(cafeId, CLOSE);

        return mockMvc.perform(MockMvcRequestBuilders.post(url)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON));
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

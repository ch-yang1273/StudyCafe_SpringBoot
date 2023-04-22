package asc.portfolio.ascSb.firebase.infra;

import asc.portfolio.ascSb.firebase.dto.FCMMessageDto;
import asc.portfolio.ascSb.firebase.service.CloudMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class CloudMessageServiceImpl implements CloudMessageService {

    private final String apiURL;
    private final String firebaseCertification;

    public CloudMessageServiceImpl(@Value("${firebase.project-id}") String projectId,
                                   @Value("${firebase.certification}") String firebaseCertification) {
        this.apiURL = "https://fcm.googleapis.com/v1/projects/" + projectId + "/messages:send";
        this.firebaseCertification = firebaseCertification;
    }

    @Override
    public void sendMessageToSpecificUser(String deviceToken, String title, String body) throws IOException {
        String message = makeMessage(deviceToken, title, body);

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = RequestBody.create(message,
                MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(apiURL)
                .post(requestBody)
                .addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + getAccessToken())
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/json; UTF-8")
                .build();

        Response response = client.newCall(request).execute();
    }

    private String makeMessage(String targetToken, String title, String body) {
        FCMMessageDto fcmMessageDto = FCMMessageDto.builder()
                .message(FCMMessageDto.Message.builder()
                        .token(targetToken)
                        .notification(FCMMessageDto.Notification.builder()
                                .title(title)
                                .body(body)
                                .image(null)
                                .build()
                        ).build()).validateOnly(false).build();

        try {
            return new ObjectMapper().writeValueAsString(fcmMessageDto);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JsonProcessingException", e);
        }
    }

    private String getAccessToken() throws IOException {
        // Firebase springBoot private secret key
        GoogleCredentials googleCredentials = GoogleCredentials
                .fromStream(new ClassPathResource(firebaseCertification).getInputStream())
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));

        googleCredentials.refreshIfExpired();
        return googleCredentials.getAccessToken().getTokenValue();
    }
}

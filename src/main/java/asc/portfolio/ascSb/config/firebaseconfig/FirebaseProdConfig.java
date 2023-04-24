package asc.portfolio.ascSb.config.firebaseconfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Profile("prod")
@Component
public class FirebaseProdConfig implements FirebaseConfig {

    public void initialize() throws IOException {
        log.info("initialize FirebaseProdConfig");

        // from GOOGLE_APPLICATION_CREDENTIALS 환경 변수
        // ref : https://firebase.google.com/docs/admin/setup?hl=ko#linux-or-macos
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .build();

        FirebaseApp.initializeApp(options);
    }
}

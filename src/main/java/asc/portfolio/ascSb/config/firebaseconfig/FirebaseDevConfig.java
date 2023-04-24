package asc.portfolio.ascSb.config.firebaseconfig;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Profile("dev")
@Component
public class FirebaseDevConfig implements FirebaseConfig {

    @Override
    public void initialize() throws IOException {
        log.info("initialize FirebaseDevConfig");

        ClassPathResource resource = new ClassPathResource("firebase/firebase-certification.json");
        InputStream inputStream = resource.getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build();

        FirebaseApp.initializeApp(options);
    }
}

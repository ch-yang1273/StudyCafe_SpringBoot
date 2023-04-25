package asc.portfolio.ascSb.config.firebaseconfig;

import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class firebaseConfiguration {

    public firebaseConfiguration(FirebaseConfig firebaseConfig) throws IOException {
        // CI 테스트를 위한 구조
        firebaseConfig.initialize();
    }
}

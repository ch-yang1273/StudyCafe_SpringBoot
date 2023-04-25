package asc.portfolio.ascSb.config.firebaseconfig;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Primary
@Component
public class FirebaseTestConfig implements FirebaseConfig {

    @Override
    public void initialize() throws IOException {
        log.info("initialize FirebaseTestConfig");

        // todo: TestServiceAccount.EDITOR 에서 지정한 파일이 있어야 함. 사용은 못하겠음. 추후 삭제 예정.
//        GoogleCredentials TestCertCredential = TestUtils.getCertCredential(TestServiceAccount.EDITOR.asStream());
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(TestCertCredential)
//                .build();
//
//        FirebaseApp.initializeApp(options);
    }
}

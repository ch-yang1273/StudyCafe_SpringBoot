package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.user.infra.MessageDigestPasswordEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PasswordEncoderTest {

    @Test
    public void encryptPassword() {
        PasswordEncoder passwordEncoder = new MessageDigestPasswordEncoder();
        String id = "loginId";
        String password = "loginPassword";
        String encrypted = passwordEncoder.encryptPassword(id, password);

        Assertions.assertThat(encrypted).isNotEqualTo(password);
    }
}
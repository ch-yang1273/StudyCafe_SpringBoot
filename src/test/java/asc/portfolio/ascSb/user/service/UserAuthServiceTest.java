package asc.portfolio.ascSb.user.service;

import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.user.domain.PasswordEncoder;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.infra.MessageDigestPasswordEncoder;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class UserAuthServiceTest {

    @Test
    public void signUp() {
        //given
        User user = UserFixture.DAISY.toUser();
        PasswordEncoder encoder = new MessageDigestPasswordEncoder();
        String password = user.getPassword();

        //when
        user.encryptPassword(encoder);

        //then
        assertThat(password).isNotEqualTo(user.getPassword());
    }
}
package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.user.exception.UserErrorData;
import asc.portfolio.ascSb.user.exception.UserException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.Random;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "USER_TABLE")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Size(min = 8, max = 16)
    @Column(name = "LOGIN_ID", unique = true, nullable = false)
    private String loginId;

    @Size(min = 8)
    @Column(name = "PASSWORD")
    private String password;

    @Email
    @Column(name = "EMAIL", unique = true)
    private String email;

    @Column(name = "NAME")
    private String name;

    @Column(name = "QR")
    private String qrCode;

    @Column(name = "USER_ROLE", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoleType role;

    @Builder
    public User(String loginId, String password, String email, String name, UserRoleType role) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.name = name;
        this.role = role;
        this.qrCode = createQrString();
    }

    public void encryptPassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encryptPassword(this.loginId, this.password);
    }

    public void checkPassword(PasswordEncoder passwordEncoder, String loginId, String rawPassword) {
        String encrypt = passwordEncoder.encryptPassword(loginId, rawPassword);
        if (!this.password.equals(encrypt)) {
            throw new UserException(UserErrorData.USER_WRONG_PASSWORD);
        }
    }

    public void changeRole(UserRoleType role) {
        this.role = role;
    }

    public String createQrString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}

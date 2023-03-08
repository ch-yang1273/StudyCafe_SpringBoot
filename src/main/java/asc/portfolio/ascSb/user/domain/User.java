package asc.portfolio.ascSb.user.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.exception.UnknownUserException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
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

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "C_ID")
    private Cafe cafe;

    @Size(min = 8, max = 16)
    @Column(name = "L_ID", unique = true, nullable = false)
    private String loginId;

    @Size(min = 8)
    private String password;

    @Email
    @Column(unique = true)
    private String email;

    private String name;
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
            throw new UnknownUserException();
        }
    }

    public void changeCafe(Cafe cafe) {
        this.cafe = cafe;
    }

    public String createQrString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }
}

package asc.portfolio.ascSb.domain.user;

import asc.portfolio.ascSb.domain.BaseTimeEntity;
import asc.portfolio.ascSb.domain.cafe.Cafe;
import asc.portfolio.ascSb.domain.ticket.Ticket;
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

  //TODO ChangePassword 따로 만들기.

  public void changeCafe(Cafe cafe) {
    this.cafe = cafe;
  }

  public String createQrString() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();

    String generatedString = random.ints(leftLimit,rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(targetStringLength)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();

    return generatedString;
  }
}

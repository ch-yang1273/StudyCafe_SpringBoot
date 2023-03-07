package asc.portfolio.ascSb.adminfcmtoken.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// FCM TOKEN 관리를 위한 테이블, ADMIN TOKEN은 만약의 경우를 대비해 메모리상(Redis)이 아니라 RDB에 보관한다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "ADMIN_FCM_TOKEN")
public class AdminFCMToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AFT_ID", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    @OneToOne
    @JoinColumn(name = "C_ID", nullable = false)
    private Cafe cafe;

    @Column(name = "FCM_TOKEN", unique = true, nullable = false)
    private String fCMToken;

    @Builder
    public AdminFCMToken(User user, Cafe cafe, String fCMToken) {
        this.user = user;
        this.cafe = cafe;
        this.fCMToken = fCMToken;
    }

    public void setFCMToken(String fCMToken) {
        this.fCMToken = fCMToken;
    }
}

package asc.portfolio.ascSb.push.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "FCM_TOKEN")
public class DeviceToken {

    @Id
    @Column(name = "TOKEN_ID")
    private Long id;

    @Column(name = "TOKEN")
    private String token;

    @Column(name = "expired_at")
    private LocalDate expiredAt;

    @Builder
    public DeviceToken(Long userId, String token, LocalDate expiredAt) {
        this.id = userId;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public void updateToken(String token, LocalDate expiredAt) {
        this.token = token;
        this.expiredAt = expiredAt;
    }
}

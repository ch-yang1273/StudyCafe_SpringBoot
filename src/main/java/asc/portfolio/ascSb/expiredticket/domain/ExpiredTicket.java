package asc.portfolio.ascSb.expiredticket.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "EXPIRED_TICKET")
public class ExpiredTicket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "E_T_ID", nullable = false)
    private Long id;

    @Column(name = "ProductLabel", unique = true)
    private String productLabel;

    private Long cafeId;

    private Long userId;

    @Builder
    public ExpiredTicket(String productLabel, Long cafeId, Long userId) {
        this.productLabel = productLabel;
        this.cafeId = cafeId;
        this.userId = userId;
    }
}

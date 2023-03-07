package asc.portfolio.ascSb.cafe.domain;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter // test 용
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CAFE")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "C_ID", nullable = false)
    private Long id;

    @Column(unique = true, nullable = false)
    private String cafeName;

    private String cafeArea;

    private String cafeState; // 카페 영업 여부

    private int businessHour; // 영업시간

    @Builder
    public Cafe(String cafeName, String cafeArea, String cafeState, int businessHour) {
        this.cafeName = cafeName;
        this.cafeArea = cafeArea;
        this.cafeState = cafeState;
        this.businessHour = businessHour;
    }
}

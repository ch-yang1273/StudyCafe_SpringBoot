package asc.portfolio.ascSb.cafe.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CAFE")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAFE_ID")
    private Long id;

    @Column(unique = true, nullable = false)
    private String cafeName;

    private String cafeArea;

    @Column(name = "IS_OPEN")
    private boolean isOpen;

    @Builder
    public Cafe(String cafeName, String cafeArea) {
        this.cafeName = cafeName;
        this.cafeArea = cafeArea;
        this.isOpen = false;
    }

    public void openCafe() {
        this.isOpen = true;
    }

    public void closeCafe() {
        //todo : 사용 중인 seat도 종료
        this.isOpen = false;
    }
}

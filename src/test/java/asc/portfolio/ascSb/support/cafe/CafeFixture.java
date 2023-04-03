package asc.portfolio.ascSb.support.cafe;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Getter;

@Getter
public enum CafeFixture {

    CAFE_A(1L, "CafeA", "AreaA"),
    CAFE_C(2L, "CafeC", "AreaC"),
    CAFE_E(3L, "CafeE", "AreaE"),
    CAFE_B(4L, "CafeB", "AreaB"),
    CAFE_D(5L, "CafeD", "AreaD");

    private final Long id;
    private final String cafeName;
    private final String cafeArea;

    CafeFixture(Long id, String cafeName, String cafeArea) {
        this.id = id;
        this.cafeName = cafeName;
        this.cafeArea = cafeArea;
    }

    public Cafe toCafe(User user) {
        return Cafe.builder()
                .id(id)
                .adminId(user.getId())
                .cafeName(cafeName)
                .cafeArea(cafeArea)
                .build();
    }
}

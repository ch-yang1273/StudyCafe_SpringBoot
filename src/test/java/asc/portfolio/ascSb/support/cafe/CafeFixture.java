package asc.portfolio.ascSb.support.cafe;

import asc.portfolio.ascSb.cafe.domain.Cafe;

public enum CafeFixture {

    CAFE_A("CafeA", "AreaA"),
    CAFE_C("CafeC", "AreaC"),
    CAFE_E("CafeE", "AreaE"),
    CAFE_B("CafeB", "AreaB"),
    CAFE_D("CafeD", "AreaD");

    private final String cafeName;
    private final String cafeArea;

    CafeFixture(String cafeName, String cafeArea) {
        this.cafeName = cafeName;
        this.cafeArea = cafeArea;
    }

    public Cafe toCafe() {
        return Cafe.builder()
                .cafeName(cafeName)
                .cafeArea(cafeArea)
                .build();
    }
}

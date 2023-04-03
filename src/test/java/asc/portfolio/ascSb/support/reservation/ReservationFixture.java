package asc.portfolio.ascSb.support.reservation;

public enum ReservationFixture {
    RESERVATION_A(1L),
    RESERVATION_B(2L),
    RESERVATION_C(3L),
    RESERVATION_D(4L)
    ;
    private final Long id;

    ReservationFixture(Long id) {
        this.id = id;
    }
}

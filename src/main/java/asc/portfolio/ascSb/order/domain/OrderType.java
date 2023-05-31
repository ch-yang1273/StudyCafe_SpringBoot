package asc.portfolio.ascSb.order.domain;

// todo : 이렇게 enum으로 하면 변경이 어렵다. Product 엔티티 만들고 넣어둬야 한다.
public enum OrderType {
    FIXED_TERM_ONE_DAY("당일권", 10000, "FIXED_TERM", 1, 0),
    FIXED_TERM_ONE_WEEK("7일권", 15000, "FIXED_TERM", 7, 0),
    FIXED_TERM_TWO_WEEK("14일권", 30000, "FIXED_TERM", 14,0),
    FIXED_TERM_THREE_WEEK("21일권", 40000, "FIXED_TERM", 21,0),
    FIXED_TERM_FOUR_WEEK("28일권", 50000, "FIXED_TERM", 28, 0),

    PART_TIME_ONE_HOUR("1시간권", 1000, "PART_TIME", 0, 1),
    PART_TIME_FOUR_HOUR("4시간권", 7000, "PART_TIME", 0, 4),
    PART_TIME_TEN_HOUR("10시간권", 10000, "PART_TIME", 0, 10),
    PART_TIME_FIFTY_HOUR("50시간권", 35000, "PART_TIME", 0, 50),
    PART_TIME_HUNDRED_HOUR("100시간권", 50000, "PART_TIME", 0, 100);

    private final String value;
    private final int price;
    private final String label;
    private final int days;
    private final Long minute;

    OrderType(String value, int price, String label, int days , int hour) {
        this.value = value;
        this.price = price;
        this.label = label;
        this.days = days;
        this.minute = hour * 60L;
    }

    public boolean isFixedTerm() {
        return label.equals("FIXED-TERM");
    }

    public boolean isPartTime() {
        return label.equals("PART-TIME");
    }

    public String getValue() {
        return value;
    }

    public int getPrice() {
        return price;
    }

    public String getLabel() {
        return label;
    }

    public int getDays() {
        return days;
    }

    public Long getMinute() {
        return minute;
    }
}

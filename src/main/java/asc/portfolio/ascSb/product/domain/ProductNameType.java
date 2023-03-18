package asc.portfolio.ascSb.product.domain;

public enum ProductNameType {
    TODAY_FIXED_TERM_TICKET("당일권", 10000, "FIXED-TERM"),
    WEEK_FIXED_TERM_TICKET("7일권", 15000, "FIXED-TERM"),
    TWO_WEEK_FIXED_TERM_TICKET("14일권", 30000, "FIXED-TERM"),
    THREE_WEEK_FIXED_TERM_TICKET("21일권", 40000, "FIXED-TERM"),
    FOUR_WEEK_FIXED_TERM_TICKET("28일권", 50000, "FIXED-TERM"),
    ONE_HOUR_PART_TIME_TICKET("1시간권", 1000, "PART-TIME"),
    FOUR_HOUR_PART_TIME_TICKET("4시간권", 7000, "PART-TIME"),
    TEN_HOUR_PART_TIME_TICKET("10시간권", 10000, "PART-TIME"),
    FIFTY_HOUR_PART_TIME_TICKET("50시간권", 35000, "PART-TIME"),
    HUNDRED_HOUR_PART_TIME_TICKET("100시간권", 50000, "PART-TIME");

    private final String value;
    private final int price;
    private final String label;
    ProductNameType(String value, int price, String label) {
        this.value = value;
        this.price = price;
        this.label = label;
    }
    public String getValue() { return value; }
    public int getPrice() { return price; }
    public String getLabel() { return label; }

}

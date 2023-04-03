package asc.portfolio.ascSb.ticket.domain;

public enum TicketType {
    FIXED_TERM("FIXED_TERM"),
    PART_TERM("PART_TERM")
    ;

    private final String name;

    TicketType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

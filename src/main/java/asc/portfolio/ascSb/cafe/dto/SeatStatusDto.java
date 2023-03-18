package asc.portfolio.ascSb.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SeatStatusDto {

    private Long id;
    private boolean isReserved;
}

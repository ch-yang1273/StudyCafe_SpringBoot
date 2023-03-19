package asc.portfolio.ascSb.cafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CafeResponseDto {

    private Long cafeId;
    private String cafeNames;
    private String cafeArea;
}

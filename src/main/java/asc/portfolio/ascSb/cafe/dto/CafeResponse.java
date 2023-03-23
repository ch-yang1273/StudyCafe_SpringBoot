package asc.portfolio.ascSb.cafe.dto;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CafeResponse {

    private Long cafeId;
    private String cafeNames;
    private String cafeArea;

    public static CafeResponse of(Cafe cafe) {
        return new CafeResponse(cafe.getId(), cafe.getCafeName(), cafe.getCafeArea());
    }
}

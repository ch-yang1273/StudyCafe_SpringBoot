package asc.portfolio.ascSb.faq.dto;

import asc.portfolio.ascSb.faq.domain.FAQCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class FAQResponseDto {

    private FAQCategory category;

    private List<FAQSelectedDto> FAQ;

    public FAQResponseDto(FAQCategory category, List<FAQSelectedDto> fAQ) {
        this.category = category;
        this.FAQ = fAQ;
    }

}

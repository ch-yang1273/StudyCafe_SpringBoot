package asc.portfolio.ascSb.faq.dto;

import asc.portfolio.ascSb.faq.domain.FAQ;
import asc.portfolio.ascSb.faq.domain.FAQCategory;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FAQRequestDto {

    private FAQCategory category;
    private String question;
    private String answer;

    public FAQ toEntity(User user) {
        return FAQ.builder()
                .cafe(user.getCafe())
                .user(user)
                .fAQCategory(category)
                .question(question)
                .answer(answer)
                .build();
    }
}

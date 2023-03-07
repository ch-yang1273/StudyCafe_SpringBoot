package asc.portfolio.ascSb.faq.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FAQSelectedDto {

    private String question;
    private String answer;

    public FAQSelectedDto(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }
}

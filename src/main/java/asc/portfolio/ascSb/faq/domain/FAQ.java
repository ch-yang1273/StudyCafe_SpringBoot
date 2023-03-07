package asc.portfolio.ascSb.faq.domain;

import asc.portfolio.ascSb.common.domain.BaseTimeEntity;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "FAQ")
public class FAQ extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "F_ID", nullable = false)
    private Long id;

    @JoinColumn(name = "C_ID")
    @ManyToOne
    private Cafe cafe;

    @ManyToOne
    @JoinColumn(name ="USER_ID")
    private User user;

    @Enumerated(EnumType.STRING)
    private FAQCategory fAQcategory;

    private String question;

    private String answer;


    @Builder
    public FAQ(Cafe cafe, User user, FAQCategory fAQCategory, String question, String answer) {
        this.cafe = cafe;
        this.user = user;
        this.fAQcategory = fAQCategory;
        this.question = question;
        this.answer = answer;
    }

}

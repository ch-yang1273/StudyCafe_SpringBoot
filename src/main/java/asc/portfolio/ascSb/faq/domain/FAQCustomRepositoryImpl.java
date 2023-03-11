package asc.portfolio.ascSb.faq.domain;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.faq.dto.FAQResponseDto;
import asc.portfolio.ascSb.faq.dto.FAQSelectedDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static asc.portfolio.ascSb.faq.domain.QFAQ.*;

@RequiredArgsConstructor
@Repository
public class FAQCustomRepositoryImpl implements FAQCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<FAQResponseDto> findAllFAQListGroupByCategory(User user) throws NullPointerException {
        List<FAQResponseDto> faqResponseDtos = new ArrayList<>();

        for (FAQCategory faqCategory: FAQCategory.values()) {
            List<FAQSelectedDto> fAQList =
                    query
                    .select(Projections.bean(FAQSelectedDto.class, fAQ.question, fAQ.answer))
                    .from(fAQ)
                    .where(fAQ.fAQcategory.eq(faqCategory), fAQ.cafe.eq(user.getCafe()))
                    .orderBy(fAQ.id.desc())
                    .fetch();

            if(!fAQList.isEmpty()) {
                faqResponseDtos.add(new FAQResponseDto(faqCategory, fAQList));
            }
        }
        return faqResponseDtos;
    }
}


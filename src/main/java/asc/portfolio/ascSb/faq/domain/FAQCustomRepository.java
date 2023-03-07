package asc.portfolio.ascSb.faq.domain;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.faq.dto.FAQResponseDto;

import java.util.List;

public interface FAQCustomRepository {

    List<FAQResponseDto> findAllFAQListGroupByCategory(User user);

}

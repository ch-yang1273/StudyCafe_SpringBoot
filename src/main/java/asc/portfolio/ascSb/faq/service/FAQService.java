package asc.portfolio.ascSb.faq.service;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.faq.dto.FAQRequestDto;
import asc.portfolio.ascSb.faq.dto.FAQResponseDto;

import java.util.List;

public interface FAQService {

    List<FAQResponseDto> cafeFAQList(User user);

    Long saveFAQ(User user, FAQRequestDto faqRequestDto);
}

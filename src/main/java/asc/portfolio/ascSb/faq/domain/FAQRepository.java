package asc.portfolio.ascSb.faq.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FAQRepository extends JpaRepository<FAQ, Long>, FAQCustomRepository {

}

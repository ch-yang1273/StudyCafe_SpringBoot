package asc.portfolio.ascSb.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductCustomRepository {

    Optional<Product> findByLabelContains(String label);
}

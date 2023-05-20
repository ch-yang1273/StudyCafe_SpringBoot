package asc.portfolio.ascSb.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    Optional<Orders> findByReceiptId(String receiptId);

    Optional<Orders> findByProductLabel(String productLabel);
}

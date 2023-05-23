package asc.portfolio.ascSb.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepository extends JpaRepository<Orders, Long> {

    List<Orders> findByUserId(Long userId);

    List<Orders> findByUserIdAndCafeId(Long userId, Long cafeId);

    Optional<Orders> findByReceiptId(String receiptId);

    Optional<Orders> findByProductLabel(String productLabel);
}

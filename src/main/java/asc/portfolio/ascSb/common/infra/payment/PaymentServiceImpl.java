package asc.portfolio.ascSb.common.infra.payment;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.service.ProductService;
import asc.portfolio.ascSb.ticket.service.TicketService;
import asc.portfolio.ascSb.common.infra.bootpay.dto.BootPayOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.transaction.TransactionalException;

//todo : 없어져야 할 코드
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final ProductService productService;
    private final TicketService ticketService;

    // 결제 검증 완료된 데이터 DML 시의 transaction 통일을 위한 method
    @Transactional(propagation = Propagation.REQUIRED)
    public boolean modifyAndAddValidPayment(Orders orders, Long userId, BootPayOrderDto dto) {
        try {
            orders.completeOrder();
            productService.saveProduct(userId, dto, orders);
            ticketService.saveProductToTicket(userId, dto, orders);
        } catch (TransactionalException e) {
            log.info("transaction is failed");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

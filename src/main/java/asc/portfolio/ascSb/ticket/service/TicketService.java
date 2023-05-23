package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.domain.Product;
import asc.portfolio.ascSb.product.domain.ProductFinder;
import asc.portfolio.ascSb.product.domain.ProductType;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketFinder;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.ticket.domain.TicketStatus;
import asc.portfolio.ascSb.ticket.domain.TicketType;
import asc.portfolio.ascSb.ticket.exception.TicketErrorData;
import asc.portfolio.ascSb.ticket.exception.TicketException;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.ticket.dto.TicketStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final TicketFinder ticketFinder;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;

    private final CurrentTimeProvider currentTimeProvider;

    // todo 삭제
    private final ProductFinder productFinder;

    @Transactional(readOnly = true)
    public List<TicketStatusResponse> getAllUserTickets(Long userId) {
        List<Ticket> tickets = ticketFinder.findAllByUserId(userId);
        return tickets.stream()
                .map(TicketStatusResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketStatusResponse userValidTicket(Long userId, String cafeName) {
        Cafe cafe = cafeFinder.findByCafeName(cafeName);

        Ticket ticket = ticketFinder.findTicketByUserIdAndCafeIdAndInUseStatus(userId, cafe.getId());
        return TicketStatusResponse.of(ticket);
    }

    //todo 코드 너무 길다.
    @Transactional
    public void saveProductToTicket(Long userId, Orders orders) {
        User user = userFinder.findById(userId);
        ProductType productType = orders.getProductType();

        Optional<Ticket> findTicketOpt = ticketRepository.findTicketByUserIdAndCafeIdAndTicketStatus(userId, orders.getCafeId(), TicketStatus.IN_USE);

        // todo : ticket 연장과 새로 생성하는 코드가 분리. Ticket 연장 기능은 제공해야겠다.
        if (findTicketOpt.isPresent()) {
            // 사용중인 티켓에 시간(기간)추가
            Ticket ticket = findTicketOpt.get();

            if (productType.isPartTime()) { // 시간제 티켓
                if (ticket.isNotOfType(TicketType.PART_TERM)) {
                    throw new TicketException(TicketErrorData.CANNOT_EXTEND_DIFFERENT_TYPE);
                }
                ticket.extendRemainingMinute(orders.getProductType().getMinute()); // todo : 캡슐화 ex) orders.getMinute
            } else if (productType.isFixedTerm()) {
                if (ticket.isNotOfType(TicketType.FIXED_TERM)) {
                    throw new TicketException(TicketErrorData.CANNOT_EXTEND_DIFFERENT_TYPE);
                }
                ticket.extendExpiryDate(orders.getProductType().getDays()); // todo : 이것도 지저분합니다.
            }
        }

        // todo : TickeType에 따라 생성 코드 분리. FixedTypeof, PartTypeOf
        int days = orders.getProductType().getDays();
        if (productType.isPartTime()) {
            Ticket ticket = Ticket.builder()
                    .userId(user.getId())
                    .cafeId(orders.getCafeId())
                    .status(TicketStatus.IN_USE)
                    .price(orders.getPrice())
                    .ticketType(TicketType.PART_TERM)
                    .expiryDate(null)
                    .totalDuration(orders.getProductType().getMinute())
                    .remainMinute(orders.getProductType().getMinute())
                    .productLabel(orders.getProductLabel())
                    .build();

            ticketRepository.save(ticket);
        } else if (productType.isFixedTerm()) {
            Ticket ticket = Ticket.builder()
                    .userId(user.getId())
                    .cafeId(orders.getCafeId())
                    .status(TicketStatus.IN_USE)
                    .price(orders.getPrice())
                    .ticketType(TicketType.FIXED_TERM)
                    .expiryDate(currentTimeProvider.localDateNow().plusDays(days))
                    .totalDuration(null)
                    .remainMinute(null)
                    .productLabel(orders.getProductLabel())
                    .build();

            ticketRepository.save(ticket);
        }
    }

    @Transactional
    public List<TicketStatusResponse> lookupUserTickets(String userLoginId, Long adminId) {
        User user = userFinder.findByLoginId(userLoginId);
        Cafe cafe = cafeFinder.findByAdminId(adminId);

        return ticketFinder.findAllByUserIdAndCafeId(user.getId(), cafe.getId())
                .stream()
                .map(TicketStatusResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void setInvalidTicket(Long productId) {
        Product product = productFinder.findById(productId);
        Ticket deleteTicket = ticketRepository.findByProductLabelContains(product.getLabel());
        deleteTicket.changeTicketStateToInvalid();
        ticketRepository.save(deleteTicket);
    }
}

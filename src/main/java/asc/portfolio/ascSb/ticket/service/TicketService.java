package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.order.domain.OrderType;
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
        OrderType orderType = orders.getOrderType();

        Optional<Ticket> findTicketOpt = ticketRepository.findTicketByUserIdAndCafeIdAndTicketStatus(userId, orders.getCafeId(), TicketStatus.IN_USE);

        // todo : ticket 연장과 새로 생성하는 코드가 분리. Ticket 연장 기능은 제공해야겠다.
        if (findTicketOpt.isPresent()) {
            // 사용중인 티켓에 시간(기간)추가
            Ticket ticket = findTicketOpt.get();

            if (orderType.isPartTime()) { // 시간제 티켓
                if (ticket.isNotOfType(TicketType.PART_TERM)) {
                    throw new TicketException(TicketErrorData.CANNOT_EXTEND_DIFFERENT_TYPE);
                }
                ticket.extendRemainingMinute(orders.getOrderType().getMinute()); // todo : 캡슐화 ex) orders.getMinute
            } else if (orderType.isFixedTerm()) {
                if (ticket.isNotOfType(TicketType.FIXED_TERM)) {
                    throw new TicketException(TicketErrorData.CANNOT_EXTEND_DIFFERENT_TYPE);
                }
                ticket.extendExpiryDate(orders.getOrderType().getDays()); // todo : 이것도 지저분합니다.
            }
        }

        // todo : TickeType에 따라 생성 코드 분리. FixedTypeof, PartTypeOf
        int days = orders.getOrderType().getDays();
        if (orderType.isPartTime()) {
            Ticket ticket = Ticket.builder()
                    .userId(user.getId())
                    .cafeId(orders.getCafeId())
                    .status(TicketStatus.IN_USE)
                    .price(orders.getPrice())
                    .ticketType(TicketType.PART_TERM)
                    .expiryDate(null)
                    .totalDuration(orders.getOrderType().getMinute())
                    .remainMinute(orders.getOrderType().getMinute())
                    .productLabel(orders.getProductLabel())
                    .build();

            ticketRepository.save(ticket);
        } else if (orderType.isFixedTerm()) {
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
    public void setInvalidTicket(Long orderId) {
        Ticket ticket = ticketFinder.findByOrderId(orderId);
        ticket.changeTicketStateToInvalid();
    }
}

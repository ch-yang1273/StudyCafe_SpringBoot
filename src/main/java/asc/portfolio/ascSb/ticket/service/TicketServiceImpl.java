package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.follow.domain.Follow;
import asc.portfolio.ascSb.follow.domain.FollowingRepository;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.product.domain.ProductRepository;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketFinder;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.ticket.domain.TicketStatus;
import asc.portfolio.ascSb.ticket.domain.TicketType;
import asc.portfolio.ascSb.ticket.exception.TicketErrorData;
import asc.portfolio.ascSb.ticket.exception.TicketException;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserFinder;
import asc.portfolio.ascSb.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.ticket.dto.TicketForAdminResponse;
import asc.portfolio.ascSb.ticket.dto.TicketStatusResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(propagation = Propagation.REQUIRED)
@RequiredArgsConstructor
@Slf4j
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final TicketFinder ticketFinder;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;
    private final CurrentTimeProvider currentTimeProvider;

    private final FollowingRepository followingRepository; // todo : 삭제
    private final ProductRepository productRepository;

    @Override
    public TicketStatusResponse userValidTicket(Long userId, String cafeName) {
        Cafe cafe = cafeFinder.findByCafeName(cafeName);

        Ticket ticket = ticketFinder.findTicketByUserIdAndCafeIdAndInUseStatus(userId, cafe.getId());
        return TicketStatusResponse.of(ticket);
    }

    //todo 코드 너무 길다.
    @Override
    public void saveProductToTicket(Long userId, BootPayOrderDto bootPayOrderDto, Orders orders) {
        User user = userFinder.findById(userId);

        // todo 수정 필요. 카페는 Product나 orders가 갖고 있어야 했고, 그 엔티티에서 가져와야 했다.
        Follow follow = followingRepository.findById(userId).orElseThrow();
        Cafe cafe = cafeFinder.findById(follow.getCafeId());

        Optional<Ticket> findTicketOpt = ticketRepository.findTicketByUserIdAndCafeIdAndInUseStatus(userId, cafe.getId());

        // todo : ticket 연장과 새로 생성하는 코드가 분리. Ticket 연장 기능은 제공해야겠다.
        if (findTicketOpt.isPresent()) {
            // 사용중인 티켓에 시간(기간)추가
            Ticket ticket = findTicketOpt.get();

            if (bootPayOrderDto.getData().getName().contains("시간")) { // 시간제 티켓
                if (ticket.isNotOfType(TicketType.PART_TERM)) {
                    throw new TicketException(TicketErrorData.CANNOT_EXTEND_DIFFERENT_TYPE);
                }
                ticket.extendRemainingMinute(orders.getOrderProductNameType().getMinute()); // todo : 캡슐화 ex) orders.getMinute
            } else if (bootPayOrderDto.getData().getName().contains("일")) {
                if (ticket.isNotOfType(TicketType.FIXED_TERM)) {
                    throw new TicketException(TicketErrorData.CANNOT_EXTEND_DIFFERENT_TYPE);
                }
                ticket.extendExpiryDate(orders.getOrderProductNameType().getDays()); // todo : 이것도 지저분합니다.
            }
        }

        // todo : TickeType에 따라 생성 코드 분리. FixedTypeof, PartTypeOf
        int days = orders.getOrderProductNameType().getDays();
        if (bootPayOrderDto.getData().getName().contains("시간")) {
            Ticket ticket = Ticket.builder()
                    .userId(user.getId())
                    .cafeId(cafe.getId())
                    .status(TicketStatus.IN_USE)
                    .price(bootPayOrderDto.getData().getPrice())
                    .ticketType(TicketType.PART_TERM)
                    .expiryDate(null)
                    .totalDuration(orders.getOrderProductNameType().getMinute())
                    .remainMinute(orders.getOrderProductNameType().getMinute())
                    .productLabel(orders.getProductLabel())
                    .build();

            ticketRepository.save(ticket);
        } else if (bootPayOrderDto.getData().getName().contains("일")) {
            Ticket ticket = Ticket.builder()
                    .userId(user.getId())
                    .cafeId(cafe.getId())
                    .status(TicketStatus.IN_USE)
                    .price(bootPayOrderDto.getData().getPrice())
                    .ticketType(TicketType.FIXED_TERM)
                    .expiryDate(currentTimeProvider.localDateNow().plusDays(days))
                    .totalDuration(null)
                    .remainMinute(null)
                    .productLabel(orders.getProductLabel())
                    .build();

            ticketRepository.save(ticket);
        }
    }

    @Override
    public List<TicketStatusResponse> lookupUserTickets(String userLoginId, Long adminId) {
        User user = userFinder.findByLoginId(userLoginId);
        Cafe cafe = cafeFinder.findByAdminId(adminId);

        return ticketFinder.findAllByUserIdAndCafeId(user.getId(), cafe.getId())
                .stream()
                .map(TicketStatusResponse::of)
                .collect(Collectors.toList());
    }

    @Override
    public TicketForAdminResponse adminLookUpUserValidTicket(String userLoginId, Long adminId) throws NullPointerException {
        User user = userFinder.findByLoginId(userLoginId);
        Cafe adminCafe = cafeFinder.findByAdminId(adminId);

        Ticket ticket = ticketFinder.findTicketByUserIdAndCafeIdAndInUseStatus(user.getId(), adminCafe.getId());
        String productNameType = productRepository.findByProductLabelContains(ticket.getProductLabel()).getProductNameType().getValue();
        return new TicketForAdminResponse(ticket, productNameType);
    }

    @Override
    public void setInvalidTicket(String productLabel) {
        Ticket deleteTicket = ticketRepository.findByProductLabelContains(productLabel);
        deleteTicket.changeTicketStateToInvalid();
        ticketRepository.save(deleteTicket);
    }

    @Override
    public Long updateAllValidTicketState() {
        return ticketRepository.updateAllValidTicketState();
    }
}

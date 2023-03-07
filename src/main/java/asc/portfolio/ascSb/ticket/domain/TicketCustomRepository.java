package asc.portfolio.ascSb.ticket.domain;

import asc.portfolio.ascSb.ticket.dto.TicketForUserResponseDto;
import asc.portfolio.ascSb.cafe.domain.Cafe;

import java.util.List;
import java.util.Optional;

public interface TicketCustomRepository {

    Optional<TicketForUserResponseDto> findAvailableTicketInfoByIdAndCafeName(Long id, String cafeName);

    Ticket findValidTicketInfoForAdminByUserIdAndCafeName(Long id, String cafeName);

    public Optional<Ticket> findAvailableTicketByIdAndCafe(Long id, String cafeName);

    Long verifyTicket(); // update가 진행된 isDeprecatedTicket 갯수를 return

    List<TicketForUserResponseDto> findAllTicketInfoByLoginIdAndCafe(String loginId, Cafe cafe);

    public Long updateAllValidTicketState();

    List<Ticket> findAllByIsValidTicketContains(TicketStateType ticketStateType);
}
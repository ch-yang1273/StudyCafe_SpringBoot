package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.common.infra.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.ticket.dto.TicketForAdminResponseDto;
import asc.portfolio.ascSb.ticket.dto.TicketForUserResponseDto;

import java.util.List;

public interface TicketService {

    TicketForUserResponseDto userValidTicket(Long id, String cafeName);

    Long saveProductToTicket(User user, BootPayOrderDto bootPayOrderDto, Orders orders);

    public List<TicketForUserResponseDto> lookupUserTickets(String targetUserLoginId, Cafe cafe);
//
//    Long saveTicket(TicketRequestDto dto, Long id);

    TicketForAdminResponseDto adminLookUpUserValidTicket(String userLoginID, String cafeName);

    void setInvalidTicket(String productLabel);

    public Long updateAllValidTicketState();

    List<Ticket> allInvalidTicketInfo();

    void deleteInvalidTicket(List<Ticket> tickets);


}

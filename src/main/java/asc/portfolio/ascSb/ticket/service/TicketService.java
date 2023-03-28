package asc.portfolio.ascSb.ticket.service;

import asc.portfolio.ascSb.order.domain.Orders;
import asc.portfolio.ascSb.bootpay.dto.BootPayOrderDto;
import asc.portfolio.ascSb.ticket.dto.TicketForAdminResponse;
import asc.portfolio.ascSb.ticket.dto.TicketStatusResponse;

import java.util.List;

public interface TicketService {

    TicketStatusResponse userValidTicket(Long userId, String cafeName);

    void saveProductToTicket(Long userId, BootPayOrderDto bootPayOrderDto, Orders orders);

    List<TicketStatusResponse> lookupUserTickets(String targetUserLoginId, Long adminId);

    TicketForAdminResponse adminLookUpUserValidTicket(String userLoginID, Long adminId);

    void setInvalidTicket(String productLabel);

    Long updateAllValidTicketState();
}

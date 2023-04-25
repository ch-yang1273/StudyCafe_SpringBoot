package asc.portfolio.ascSb.seat.service;

import asc.portfolio.ascSb.common.domain.CommonEventsPublisher;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.seat.domain.SeatUsageStatus;
import asc.portfolio.ascSb.seat.dto.SeatUsageEndingSoonEvent;
import asc.portfolio.ascSb.seat.dto.SeatUsageTerminatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatScheduleService {

    private final SeatRepository seatRepository;
    private final CommonEventsPublisher eventsPublisher;

    @Transactional
    public void terminateExpiredSeatsStatus(LocalDateTime now) {
        List<Seat> seats = seatRepository.findSeatsByStatusWithEndTimeBefore(
                SeatUsageStatus.ENDING_SOON,
                now);

        for (Seat seat : seats) {
            seat.scheduleSeatUsageTermination();
            eventsPublisher.raise(new SeatUsageTerminatedEvent(seat.getUserId(), seat.getId(), now));
        }

        if (seats.size() > 0) {
            log.info("Number of seats terminated: {}", seats.size());
        }
    }

    @Transactional
    public void updateApproachingExpiredSeatsStatus(LocalDateTime now) {
        List<Seat> seats = seatRepository.findSeatsByStatusWithEndTimeBefore(
                SeatUsageStatus.IN_USE,
                now.minusMinutes(10));

        for (Seat seat : seats) {
            seat.changeUsageStatusEndingSoon();
            eventsPublisher.raise(new SeatUsageEndingSoonEvent(seat.getUserId(), seat.getId(), now));
        }

        if (seats.size() > 0) {
            log.info("Number of seats approaching expiration: {}", seats.size());
        }
    }
}

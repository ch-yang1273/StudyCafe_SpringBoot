package asc.portfolio.ascSb.seat.domain;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.cafe.domain.QCafe;
import asc.portfolio.ascSb.reservation.domain.QReservation;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.reservation.domain.ReservationRepository;
import asc.portfolio.ascSb.reservation.domain.ReservationStatus;
import asc.portfolio.ascSb.ticket.domain.QTicket;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SeatCustomRepositoryImpl implements SeatCustomRepository {

    private final JPAQueryFactory query;
    private final ReservationRepository reservationRepository;

    // todo : 이게 여기 있으면 안된다. 빠르게 삭제 요망
    private final CafeFinder cafeFinder;

    @Override
    public int updateAllReservedSeatStateWithFixedTermTicket() {
        int count = 0;

        // Fixed-Term Ticket Update
        List<Seat> seatList = query
                .selectFrom(QSeat.seat)
                .join(QSeat.seat.ticket, QTicket.ticket)
                .where(QSeat.seat.isReserved.isTrue(),
                        QTicket.ticket.productLabel.contains("FIXED-TERM"),
                        QTicket.ticket.fixedTermTicket.before(LocalDateTime.now()))
                .fetch();

        // 만료된 Fixed-Term Ticket 에 따른 Seat, reservation 종료 처리
        for (Seat seatOne : seatList) {
            count++;
            log.debug("Exited by FixedTermTicket update");
            exitSeatBySeatEntity(seatOne, null);
        }

        return count;
    }

    @Override
    public int updateAllReservedSeatStateWithPartTimeTicket() {
        int count = 0;

        // Part-Time Ticket Update
        List<Seat> seatList = query
                .selectFrom(QSeat.seat)
                .join(QSeat.seat.ticket, QTicket.ticket)
                .where(QSeat.seat.isReserved.isTrue(),
                        QTicket.ticket.productLabel.contains("PART-TIME"))
                .fetch();

        for (Seat seat : seatList) {
            Cafe cafe = cafeFinder.findById(seat.getCafeId());
            Reservation rez = query
                    .selectFrom(QReservation.reservation)
                    .where(QReservation.reservation.status.eq(ReservationStatus.IN_USE),
                            QReservation.reservation.cafeId.eq(cafe.getId()),
                            QReservation.reservation.seatId.eq(seat.getId()))
                    .fetchOne();

            if (rez == null) {
                log.error("예약된 Seat에 유효한 reservation 없습니다. seat = {}, {}", cafe.getCafeName(), seat.getSeatNumber());
                return 0;
            }

//            Ticket ticketOne = rez.getTicket();
//            Long remainTime = ticketOne.getRemainingTime();
//            Long timeInUse = rez.updateTimeInUse();
//
//            if (timeInUse >= remainTime) {
//                count++;
//                log.debug("Exited by PartTimeTicket update");
//                exitSeatBySeatEntity(seat, rez);
//            }
        }

        return count;
    }

    @Override
    public int updateAllReservedStatusWithStartTime() {
        int count = 0;
        List<Reservation> reservationList = query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.status.eq(ReservationStatus.IN_USE),
                        QReservation.reservation.endTime.before(LocalDateTime.now()))
                .fetch();

        for (Reservation rez : reservationList) {
            count++;
            log.debug("Exited by StartTime update, endTime={}", rez.getEndTime());
            // todo : update 로직 수정
//            Seat findSeat = seatRepository.findById(rez.getSeatId()).orElseThrow();
//            exitSeatBySeatEntity(findSeat, rez);
        }

        return count;
    }

    @Override
    public List<Seat> getAlmostFinishedSeatListWithFixedTermTicket(Long minute) {
        //after(10분전) ~ before(9분전) ~~~ 현재

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeAfter = timeNow.plusMinutes(minute);
        LocalDateTime timeBefore = timeNow.plusMinutes(minute + 1);

        return query
                .selectFrom(QSeat.seat)
                .join(QSeat.seat.ticket, QTicket.ticket)
                .where(QSeat.seat.isReserved.isTrue(),
                        QTicket.ticket.productLabel.contains("FIXED-TERM"),
                        QTicket.ticket.fixedTermTicket.after(timeAfter),
                        QTicket.ticket.fixedTermTicket.before(timeBefore))
                .fetch();
    }

    @Override
    public List<Seat> getAlmostFinishedSeatListWithStartTime(Long minute) {
        //after(10분전) ~ before(9분전) ~~~ 현재

        LocalDateTime timeNow = LocalDateTime.now();
        LocalDateTime timeAfter = timeNow.plusMinutes(minute);
        LocalDateTime timeBefore = timeNow.plusMinutes(minute + 1);

        List<Reservation> reservationList = query
                .selectFrom(QReservation.reservation)
                .where(QReservation.reservation.status.eq(ReservationStatus.IN_USE),
                        QReservation.reservation.endTime.after(timeAfter),
                        QReservation.reservation.endTime.before(timeBefore))
                .fetch();


        List<Seat> seatList = new ArrayList<>();
        for (Reservation rez : reservationList) {
            // todo : update 로직 수정
//            Seat findSeat = seatRepository.findById(rez.getSeatId()).orElseThrow();
//            seatList.add(findSeat);
        }

        return seatList;
    }

    private void exitSeatBySeatEntity(Seat seat, Reservation reservation) {
        Cafe cafe = cafeFinder.findById(seat.getCafeId());
        String cafeName = cafe.getCafeName();

        if (reservation == null) {
            reservation = reservationRepository.findValidReservationByCafeNameAndSeatNumber(cafe.getId(), seat.getId());
        }

        Ticket ticketOne = seat.getTicket();

        log.debug("Seat 사용 종료. cafeName={}, seatNumber={}", cafeName, seat.getSeatNumber());
        //todo : 막음
//        ticketOne.exitUsingTicket(reservation.updateTimeInUse());
        seat.exitSeat();
        reservation.endUsingSeat(LocalDateTime.now());
    }

    @Override
    public Seat findByCafeAndSeatNumber(Cafe cafeObject, Integer seatNumber) {
        return query
                .selectFrom(QSeat.seat)
                .where(QSeat.seat.cafeId.eq(cafeObject.getId()), QSeat.seat.seatNumber.eq(seatNumber))
                .fetchOne();
    }

    @Override
    public Seat findByCafeNameAndSeatNumber(String cafeName, int seatNumber) {
        return query
                .select(QSeat.seat)
                .from(QSeat.seat)
                .join(QSeat.seat).on(QCafe.cafe.id.eq(QSeat.seat.cafeId))
                .where(QCafe.cafe.cafeName.eq(cafeName), QSeat.seat.seatNumber.eq(seatNumber))
                .fetchOne();
    }
}

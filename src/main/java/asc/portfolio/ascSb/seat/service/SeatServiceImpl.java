package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.infra.redis.RedisRepository;
import asc.portfolio.ascSb.follow.domain.FollowFinder;
import asc.portfolio.ascSb.reservation.domain.Reservation;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.reservation.domain.ReservationRepository;
import asc.portfolio.ascSb.ticket.domain.Ticket;
import asc.portfolio.ascSb.ticket.domain.TicketRepository;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.firebase.service.FirebaseCloudMessageService;
import asc.portfolio.ascSb.seat.dto.SeatResponseDto;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;
    private final FollowFinder followFinder; // todo 삭제
    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;
    private final RedisRepository redisRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Override
    public SeatResponseDto getMySeatStatus(Long userId) {
        Seat seat = seatRepository.findByUserId(userId).orElseThrow();
        Cafe cafe = cafeFinder.findById(seat.getCafeId());

        Ticket ticket = seat.getTicket();
        Reservation reservation = reservationRepository
                .findValidReservationByCafeNameAndSeatNumber(cafe.getId(), seat.getId());


        // todo : 정보들을 Seat에서 가져와야겠다. 시간
        LocalDateTime now = LocalDateTime.now();
        if (ticket.isValidFixedTermTicket()) {
            return SeatResponseDto.setFixedTermSeat(
                    seat.getSeatNumber(),
                    reservation.getStartTime(),
                    reservation.getUsageMinute(now),
                    ticket.getFixedTermTicket());
        } else if (ticket.isValidPartTimeTicket()) {
            return SeatResponseDto.setPartTimeSeat(
                    seat.getSeatNumber(),
                    reservation.getStartTime(),
                    reservation.getUsageMinute(now),
                    ticket.getPartTimeTicket(),
                    ticket.getRemainingTime());
        } else {
            log.error("IllegalState Ex. Ticket id ={}", ticket.getId());
            throw new IllegalStateException("IllegalState Ex. Ticket id =" + ticket.getId());
        }
    }

    @Override
    public Boolean exitSeat(Long userId) {
        User user = userFinder.findById(userId);

        //reservation 수정
        List<Reservation> reservationList = reservationRepository.findValidReservationByLoginId(user.getId());
        if (reservationList != null) {
            if (reservationList.size() > 1) {
                log.error("Valid reservation is Over One");
            }

            for (Reservation rez : reservationList) {
                //Reservation Info Exit
                rez.endUsingSeat(LocalDateTime.now());

                //Seat Exit
                Cafe cafe = cafeFinder.findById(rez.getCafeId());
                Seat findSeat = seatRepository.findById(rez.getSeatId()).orElseThrow();
                findSeat.exitSeat();

                //Ticket Exit
                Ticket ticket = findSeat.getTicket();
                if (ticket == null) {
                    log.error("ticket == null");
                } else if (ticket.isFixedTermTicket()) {
                    ticket.exitUsingTicket(null);
                } else {
                    //todo ticket은 남은 시간만 업데이트 합시다.
//                    ticket.exitUsingTicket(timeInUse); // 사용한 시간 startTime or timeInUse
                }

            }
        }

        // seat Table 의 User_ID Unique 를 유지하기 위해, 먼저 DB에 반영
        reservationRepository.flush();
        seatRepository.flush();
        ticketRepository.flush();

        return true;
    }

    @Override
    public void exitSeatBySeatNumber(Long adminId, int seatNumber) {
        Cafe cafe = cafeFinder.findByAdminId(adminId);
        Seat findSeat = seatRepository.findByCafeAndSeatNumber(cafe, seatNumber);

        //todo exitMySeat, exitSeatBySeatNumber로 구분해야겠다.
        exitSeat(findSeat.getUserId());
    }

    @Override
    public Boolean reserveSeat(Long userId, Integer seatNumber, Long startTime) {
        // todo : user가 follow한 정보를 예약 할 것이 아니고, seatId 나 cafe 정보와 seat 정보를 받아서 예약해야 한다.
        User user = userFinder.findById(userId);
        Cafe cafe = followFinder.findFollowedCafe(userId);

        if (cafe == null) {
            log.error("선택 된 카페가 없는 유저 입니다.");
            return false;
        }

        if ((seatNumber == null) || (startTime == null)) {
            throw new NullPointerException("NullPointerException : seatNumber, startTime");
        }

        Seat findSeat = seatRepository.findByCafeAndSeatNumber(cafe, seatNumber);
        if (findSeat == null) {
            log.error("없는 좌석입니다.");
            return false;
        } else if (findSeat.isReserved()) {
            log.error("이미 예약 된 좌석입니다.");
            return false;
        }

        //사용가능한 Ticket 검색 (유효성 확인도 진행)
        Optional<Ticket> ticketOpt = ticketRepository.findAvailableTicketByIdAndCafe(user.getId(), cafe.getCafeName());
        if (ticketOpt.isEmpty()) {
            log.error("사용 가능한 티켓이 없습니다.");
            return false;
        }

        //기존에 차지하고 있던 자리가 있으면 exit
        exitSeat(userId);

        //seat 에 User, ticket 을 할당
        findSeat.reserveSeat(user.getId(), ticketOpt.get());

        //reservation 저장
        Reservation reservation = Reservation.builder()
                .user(user)
                .cafe(cafe)
                .seat(findSeat)
                .ticket(ticketOpt.get())
                .startTime(LocalDateTime.now())
                .build();
        reservationRepository.save(reservation);

        log.info("좌석 예약 성공");
        return true;
    }

    @Override
    public int updateAllReservedSeatState() {
        int count = 0;
        count += seatRepository.updateAllReservedSeatStateWithFixedTermTicket();
        count += seatRepository.updateAllReservedSeatStateWithPartTimeTicket();
        count += seatRepository.updateAllReservedStatusWithStartTime();

        return count;
    }

    private void alertFcm(List<Seat> list) {
        // TODO
        /* List<Seat>에서 FCM을 보낼 유저들을 특정 후 FireBase서버를 경유해 10분 남았다고 알림을 요청 */
        List<Long> userIds = list.stream()
                .map(Seat::getUserId)
                .collect(Collectors.toList());

        for (Long id : userIds) {
            User user = userFinder.findById(id);
            // todo 필수 값이 아닌 userName으로 검색을하고 있다. loginId로 변경
            String token = redisRepository.getValue(user.getName() + "_" + "USER" + "_FCM_TOKEN");
            try {
                firebaseCloudMessageService.sendMessageToSpecificUser(token,
                        "알라딘 스터디카페",
                        "좌석이 10분 남았습니다.");
            } catch (IOException e) {
                log.info("FCM Message sending failed");
                e.printStackTrace();
            }
        }
    }

    private void checkAlmostFinishedSeatWithFixedTermTicket() {
        List<Seat> list = seatRepository.getAlmostFinishedSeatListWithFixedTermTicket(10L);
        log.debug("Alert list size with FixedTerm Ticket = {}", list.size());

        alertFcm(list);
    }

    private void checkAlmostFinishedSeatWithStartTime() {
        List<Seat> list = seatRepository.getAlmostFinishedSeatListWithStartTime(10L);
        log.debug("Alert list size with StartTime = {}", list.size());

        alertFcm(list);
    }

    //1분 초과로 스케쥴 잡아야 중복 alert 없음
    public void alertAlmostFinishedSeat() {
        this.checkAlmostFinishedSeatWithFixedTermTicket();
        this.checkAlmostFinishedSeatWithStartTime();
    }
}

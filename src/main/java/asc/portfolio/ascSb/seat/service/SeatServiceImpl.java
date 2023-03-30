package asc.portfolio.ascSb.seat.service;
import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.domain.CafeFinder;
import asc.portfolio.ascSb.common.domain.CurrentTimeProvider;
import asc.portfolio.ascSb.common.infra.redis.RedisRepository;
import asc.portfolio.ascSb.seat.domain.Seat;
import asc.portfolio.ascSb.seat.domain.SeatFinder;
import asc.portfolio.ascSb.seat.domain.SeatRepository;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.firebase.service.FirebaseCloudMessageService;
import asc.portfolio.ascSb.seat.dto.SeatStatusResponse;
import asc.portfolio.ascSb.user.domain.UserFinder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatFinder seatFinder;
    private final UserFinder userFinder;
    private final CafeFinder cafeFinder;
    private final CurrentTimeProvider currentTimeProvider;

    private final RedisRepository redisRepository;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @Transactional(readOnly = true)
    @Override
    public SeatStatusResponse getSeatStatus(Long userId) {
        Seat seat = seatFinder.findByUserId(userId);
        Cafe cafe = cafeFinder.findById(seat.getCafeId());

        return new SeatStatusResponse(cafe, seat, currentTimeProvider.localDateTimeNow());
    }

    @Transactional(readOnly = true)
    @Override
    public List<SeatStatusResponse> getAllSeatsByCafeId(Long cafeId) {
        Cafe cafe = cafeFinder.findById(cafeId);

        return seatFinder.findAllByCafeId(cafeId)
                .stream()
                .map(seat -> new SeatStatusResponse(cafe, seat, currentTimeProvider.localDateTimeNow()))
                .collect(Collectors.toList());
    }

    @Transactional
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
                .map(seat -> seat.getUsageData().getUserId())
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
    @Transactional
    @Override
    public void alertAlmostFinishedSeat() {
        this.checkAlmostFinishedSeatWithFixedTermTicket();
        this.checkAlmostFinishedSeatWithStartTime();
    }
}

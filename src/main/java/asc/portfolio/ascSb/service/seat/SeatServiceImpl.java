package asc.portfolio.ascSb.service.seat;
import asc.portfolio.ascSb.domain.seat.SeatRepository;
import asc.portfolio.ascSb.domain.seatreservationinfo.SeatReservationInfoRepository;
import asc.portfolio.ascSb.web.dto.seat.SeatSelectResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;

    @Override
    public List<SeatSelectResponseDto> showCurrentSeatState(String cafeName) {
        return seatRepository.findSeatNumberAndSeatState(cafeName).stream()
                .map(SeatSelectResponseDto::new)
                .collect(Collectors.toList());
    }
}

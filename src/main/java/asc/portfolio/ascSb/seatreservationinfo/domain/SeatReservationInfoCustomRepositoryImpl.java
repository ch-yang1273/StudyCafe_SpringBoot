package asc.portfolio.ascSb.seatreservationinfo.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import asc.portfolio.ascSb.seatreservationinfo.dto.SeatReservationInfoSelectResponseDto;
import com.querydsl.core.types.Projections;
import org.springframework.stereotype.Repository;

import java.util.List;

import static asc.portfolio.ascSb.domain.seatreservationinfo.QSeatReservationInfo.*;

@Slf4j
@RequiredArgsConstructor
@Repository
public class SeatReservationInfoCustomRepositoryImpl implements SeatReservationInfoCustomRepository {

  private final JPAQueryFactory query;

  @Override
  public List<SeatReservationInfo> findValidSeatRezInfoByLoginId(String loginId) {

    List<SeatReservationInfo> result = query
            .select(seatReservationInfo)
            .from(seatReservationInfo)
            .where(seatReservationInfo.userLoginId.eq(loginId),
                    seatReservationInfo.isValid.eq(SeatReservationInfoStateType.VALID))
            .orderBy(seatReservationInfo.startTime.desc())
            .fetch();

    int listSize = result.size();
    if (listSize == 0) {
      // 정보 없음
      log.debug("No valid reservation. user = {}", loginId);
      return null;
    } else if (listSize > 1) {
      // Valid 상태의 reservation info 는 한개여야 한다. 런타임 예외 필요.
      log.error("The user[{}]'s valid state information exceed one.", loginId);
    }
    return result;
  }

  @Override
  public SeatReservationInfoSelectResponseDto findSeatInfoByUserIdAndCafeName(String loginId, String cafeName) {
      return query
              .select(Projections.bean(SeatReservationInfoSelectResponseDto.class,
                      seatReservationInfo.seatNumber,
                      seatReservationInfo.startTime,
                      seatReservationInfo.timeInUse,
                      seatReservationInfo.createDate))
              .from(seatReservationInfo)
              .where(seatReservationInfo.userLoginId.eq(loginId),
                      seatReservationInfo.cafeName.eq(cafeName),
              seatReservationInfo.isValid.eq(SeatReservationInfoStateType.VALID))
              .fetchOne();

  }

  @Override
  public SeatReservationInfo findValidSeatRezInfoByCafeNameAndSeatNumber(String cafeName, Integer seatNumber) {
    return query
            .selectFrom(seatReservationInfo)
            .where(seatReservationInfo.isValid.eq(SeatReservationInfoStateType.VALID),
                    seatReservationInfo.cafeName.eq(cafeName),
                    seatReservationInfo.seatNumber.eq(seatNumber))
            .fetchOne();
  }
}

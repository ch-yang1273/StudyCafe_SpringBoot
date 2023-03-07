package asc.portfolio.ascSb.seatreservationinfo.domain;

import asc.portfolio.ascSb.seatreservationinfo.dto.SeatReservationInfoSelectResponseDto;

import java.util.List;

public interface SeatReservationInfoCustomRepository {

  public List<SeatReservationInfo> findValidSeatRezInfoByLoginId(String loginId);

  SeatReservationInfoSelectResponseDto findSeatInfoByUserIdAndCafeName(String loginId, String cafeName);

  public SeatReservationInfo findValidSeatRezInfoByCafeNameAndSeatNumber(String cafeName, Integer seatNumber);
}

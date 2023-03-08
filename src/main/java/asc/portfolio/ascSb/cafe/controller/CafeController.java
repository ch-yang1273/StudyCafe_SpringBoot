package asc.portfolio.ascSb.cafe.controller;

import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import asc.portfolio.ascSb.common.auth.LoginUser;
import asc.portfolio.ascSb.cafe.service.CafeService;
import asc.portfolio.ascSb.seat.service.SeatService;
import asc.portfolio.ascSb.cafe.dto.CafeResponseDto;
import asc.portfolio.ascSb.seat.dto.SeatSelectResponseDto;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/cafe")
public class CafeController {

  private final CafeService cafeService;
  private final SeatService seatService;

  @GetMapping("")
  public List<CafeResponseDto> respCafeNames() {

    return cafeService.showAllCafeList();
  }

  @Parameter(name = "cafeName", example = "서울지점")
  @GetMapping("/state/{cafeName}")
  public ResponseEntity<List<SeatSelectResponseDto>> seatStateList(@PathVariable String cafeName) {
    if(cafeName.isEmpty()) {
      log.info("cafe 명이 비어 있습니다.");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<>(seatService.showCurrentAllSeatState(cafeName), HttpStatus.OK);
  }

  @Parameter(name = "cafeName", example = "서울지점")
  @PostMapping("/change/{cafeName}")
  public ResponseEntity<String> changeReservedUserCafe(@LoginUser User user, @PathVariable String cafeName) {
    if (user.getRole() == UserRoleType.USER) {
      String resultName = cafeService.changeReservedUserCafe(user, cafeName);
      if (resultName != null) {
        return new ResponseEntity<>(resultName, HttpStatus.OK);
      } else {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
    } else {
      log.info("Admin 유저의 카페 변경은 비활성화 되어 있습니다.");
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }
}

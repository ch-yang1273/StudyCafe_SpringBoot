package asc.portfolio.ascSb.domain;

import asc.portfolio.ascSb.domain.cafe.Cafe;
import asc.portfolio.ascSb.domain.cafe.CafeRepository;
import asc.portfolio.ascSb.commonenum.product.ProductNameType;
import asc.portfolio.ascSb.domain.product.Product;
import asc.portfolio.ascSb.domain.product.ProductRepository;
import asc.portfolio.ascSb.domain.product.ProductStateType;
import asc.portfolio.ascSb.domain.seat.Seat;
import asc.portfolio.ascSb.domain.seat.SeatRepository;
import asc.portfolio.ascSb.domain.ticket.TicketRepository;
import asc.portfolio.ascSb.domain.seatreservationinfo.SeatReservationInfoRepository;
import asc.portfolio.ascSb.domain.ticket.Ticket;
import asc.portfolio.ascSb.domain.ticket.TicketStateType;
import asc.portfolio.ascSb.domain.user.User;
import asc.portfolio.ascSb.domain.user.UserRepository;
import asc.portfolio.ascSb.domain.user.UserRoleType;
import asc.portfolio.ascSb.loginutil.LoginUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SpringBootTest
@Transactional
@Commit
public class TestDataGeneration {
  //Test 클래스의 @Transactional 은 Rollback 하니 @Commit 으로 설정

  @Autowired
  CafeRepository cafeRepository;

  @Autowired
  SeatRepository seatRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  SeatReservationInfoRepository seatReservationInfoRepository;

  @Autowired
  TicketRepository ticketRepository;

  @Autowired
  ProductRepository productRepository;

  @Autowired
  LoginUtil loginUtil;

  //Test Data
  String[] cafeName = {"서울지점", "부산지점", "인천지점", "대전지점", "광주지점", "울산지점"};

  String[] cafeArea = {
          "서울특별시",
          "부산광역시", "인천광역시", "대구광역시", "대전광역시", "광주광역시", "울산광역시",
          "세종특별자치시",
          "경기도", "강원도", "충청북도", "충청남도", "전라북도", "전라남도", "경상북도", "경상남도",
          "제주특별자치도"};

  String[] userName = {"tUser_A", "tUser_B", "tUser_C", "tUser_D", "tUser_E", "tUser_F"};
  private final int TEST_USER_COUNT_MAX = 1000;

  String[] productLabel = {"FIXED-TERM1", "FIXED-TERM2", "FIXED-TERM3", "FIXED-TERM", "FIXED-TERM",
          "FIXED-TERM", "FIXED-TERM"};
  
  @BeforeEach
  public void clearRepository() {
    productRepository.deleteAllInBatch();
    seatReservationInfoRepository.deleteAllInBatch();
    seatRepository.deleteAllInBatch();
    ticketRepository.deleteAllInBatch();
    userRepository.deleteAllInBatch();
    cafeRepository.deleteAllInBatch();
  }

  private void generateCafeSeatData() {

    for (int i = 0; i < cafeName.length; i++) {
      Cafe cafe = Cafe.builder()
              .cafeName(cafeName[i])
              .cafeArea(cafeArea[i])
              .build();

      cafeRepository.save(cafe);
    }

    generateSeatData();
  }

  private void generateSeatData() {
    List<Cafe> cafeList = cafeRepository.findAll();

    for (Cafe cafe : cafeList) {

      for(int i=0; i < 40; i ++) {

        Seat seat = Seat.builder()
                .seatNumber(i)
                .cafe(cafe)
                .build();
        seatRepository.save(seat);
      }
    }
  }

  private void generateUserData() throws Exception {

    for (String userString : userName) {
      String password = loginUtil.encryptPassword(userString + "_login", userString + "_password");
      User user = User.builder()
              .loginId(userString + "_login")
              .password(password)
              .email(userString + "@gmail.com")
              .name(userString)
              .role(UserRoleType.USER)
              .build();
      user.changeCafe(cafeRepository.findByCafeName("서울지점").orElse(null));
      userRepository.save(user);
    }

    //User Data 추가
    for (int i = 0; i < TEST_USER_COUNT_MAX; i++) {
      String userString = "tUser_" + i;
      String password = loginUtil.encryptPassword(userString + "_login", userString + "_password");
      User user = User.builder()
              .loginId(userString + "_login")
              .password(password)
              .email(userString + "@gmail.com")
              .name(userString)
              .role(UserRoleType.USER)
              .build();
      user.changeCafe(cafeRepository.findByCafeName(cafeName[i%cafeName.length]).orElse(null));
      userRepository.save(user);
    }
  }

  private void generateAdminUserData() throws Exception {
    String userName = "adminuser";
    String password = loginUtil.encryptPassword(userName, userName + "_password");

    User user = User.builder()
            .loginId(userName)
            .password(password)
            .email(userName + "@gmail.com")
            .name(userName)
            .role(UserRoleType.ADMIN)
            .build();

    user.changeCafe(cafeRepository.findByCafeName("서울지점").orElse(null));
    userRepository.save(user);
  }

  private void generateTicketData() {
    LocalDateTime date = LocalDateTime.now();

      Ticket ticket0 = Ticket.builder()
              .cafe(cafeRepository.findByCafeNameContains(cafeName[0]))
              .user(userRepository.findByLoginId(userName[0] + "_login").orElseThrow())
              .isValidTicket(TicketStateType.VALID)
              .ticketPrice(3000)
              .productLabel("FIXED-TERM"+54321)
              .fixedTermTicket(date.plusDays(28))
              .partTimeTicket(null)
              .remainingTime(null)
              .build();
      ticketRepository.save(ticket0);

      //Valid Ticket
      for (int i = 0; i < TEST_USER_COUNT_MAX; i++) {
        String userString = "tUser_" + i;
        Ticket ticket = Ticket.builder()
                .cafe(cafeRepository.findByCafeNameContains(cafeName[i%cafeName.length]))
                .user(userRepository.findByLoginId(userString + "_login").orElseThrow())
                .isValidTicket(TicketStateType.VALID)
                .ticketPrice(3000)
                .productLabel("FIXED-TERM"+i)
//                .fixedTermTicket(date.plusDays(28))
                .fixedTermTicket(date.plusSeconds(30))
                .partTimeTicket(null)
                .remainingTime(null)
                .build();
        ticketRepository.save(ticket);
      }
  }

  private void generateProductData() {
    for(int i=0; i < 1000; i ++) {
      Product product = Product.builder()
              .cafe(cafeRepository.findByCafeNameContains("서울지점"))
              .productNameType(ProductNameType.FOUR_WEEK_FIXED_TERM_TICKET)
              .user(userRepository.findByNameContains(userName[2]))
              .productState(ProductStateType.SALE)
              .description("테스트 product")
              .productPrice(13100)
              .productLabel("FIXED-TERM" + i)
              .build();
      Random random = new Random(i);
      productRepository.save(product);
      Optional<Product> changeDays = productRepository.findById((long) i);
      if(changeDays.isPresent()) {
        Product get = changeDays.get();
        get.setCreateDate(LocalDateTime.now().minusDays(random.nextInt(29)));
        productRepository.save(get);
      }
    }
  }

  @Test
  public void setTestData() throws Exception {
    Assertions.assertThat(cafeRepository.count()).isEqualTo(0);
    Assertions.assertThat(seatRepository.count()).isEqualTo(0);
    Assertions.assertThat(userRepository.count()).isEqualTo(0);

    generateCafeSeatData();
    generateUserData();
    generateAdminUserData();
    generateTicketData();
    generateProductData();
  }
}


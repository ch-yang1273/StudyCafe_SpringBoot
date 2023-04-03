package asc.portfolio.ascSb.cafe.service;

import asc.portfolio.ascSb.cafe.domain.Cafe;
import asc.portfolio.ascSb.cafe.exception.CafeException;
import asc.portfolio.ascSb.support.User.UserFixture;
import asc.portfolio.ascSb.support.cafe.CafeFixture;
import asc.portfolio.ascSb.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class CafeServiceTest {

    @Test
    @DisplayName("Admin 권한의 유저는 카페 생성 성공")
    public void registerCafe() {
        //given
        User user = UserFixture.ADMIN_BUTTERCUP.toUser();

        //when
        Cafe cafe = Cafe.constructCafeWithAuth(user, "name", "area");
    }

    @Test
    public void registerCafe_Fail() {
        //given
        User user = UserFixture.BLOO.toUser();

        //when //then
        assertThatThrownBy(() -> Cafe.constructCafeWithAuth(user, "name", "area"))
                .isInstanceOf(CafeException.class);
    }

    @Test
    @DisplayName("권한이 있는 user로 cafe open")
    public void openCafe() {
        //given
        User user = UserFixture.ADMIN_BLOSSOM.toUser();
        Cafe cafe = CafeFixture.CAFE_A.toCafe(user);

        //when
        cafe.openCafe(user);

        //then
        assertThat(cafe.isOpen()).isTrue();
    }

    @Test
    @DisplayName("권한이 없는 user로 cafe open 실패")
    public void openCafe_Fial() {
        //given
        User user = UserFixture.ADMIN_BLOSSOM.toUser();
        Cafe cafe = CafeFixture.CAFE_A.toCafe(user);
        User noAuth = UserFixture.COCO.toUser();

        //when
        assertThatThrownBy(() -> cafe.openCafe(noAuth))
                .isInstanceOf(CafeException.class);

        //then
        assertThat(cafe.isOpen()).isFalse();
    }

    @Test
    public void closeCafe() {
        //given
        User user = UserFixture.ADMIN_BLOSSOM.toUser();
        Cafe cafe = CafeFixture.CAFE_A.toCafe(user);

        //when
        cafe.closeCafe(user);
    }

    @Test
    public void closeCafe_Fail() {
        //given
        User user = UserFixture.ADMIN_BLOSSOM.toUser();
        Cafe cafe = CafeFixture.CAFE_A.toCafe(user);
        User noAuth = UserFixture.COCO.toUser();

        //when
        assertThatThrownBy(() -> cafe.closeCafe(noAuth))
                .isInstanceOf(CafeException.class);
    }
}
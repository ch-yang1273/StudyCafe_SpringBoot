package asc.portfolio.ascSb.cafe.domain;

import asc.portfolio.ascSb.cafe.exception.CafeErrorData;
import asc.portfolio.ascSb.cafe.exception.CafeException;
import asc.portfolio.ascSb.user.domain.User;
import asc.portfolio.ascSb.user.domain.UserRoleType;
import lombok.*;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "CAFE")
public class Cafe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CAFE_ID")
    private Long id;

    @Column(name = "ADMIN_ID", unique = true, nullable = false)
    private Long adminId; // user

    @Column(name = "NAME", unique = true, nullable = false)
    private String cafeName;

    @Column(name = "AREA")
    private String cafeArea;

    @Column(name = "IS_OPEN")
    private boolean isOpen;

    @Builder
    public Cafe(Long adminId, String cafeName, String cafeArea) {
        this.adminId = adminId;
        this.cafeName = cafeName;
        this.cafeArea = cafeArea;
        this.isOpen = false;
    }

    private static void authCheck(User authUser) {
        if (authUser.getRole() != UserRoleType.ADMIN) {
            throw new CafeException(CafeErrorData.CAFE_NEED_ADMIN_ROLE);
        }
    }

    public static Cafe createCafeWithAuth(User authUser, String cafeName, String cafeArea) {
        authCheck(authUser);
        return Cafe.builder()
                .adminId(authUser.getId())
                .cafeName(cafeName)
                .cafeArea(cafeArea)
                .build();
    }

    public void changeAdmin(User authUser) {
        authCheck(authUser);
        this.adminId = authUser.getId();
    }

    public void openCafe(User authUser) {
        if (this.adminId.equals(authUser.getId())) {
            this.isOpen = true;
        } else {
            throw new CafeException(CafeErrorData.UNMATCHED_ADMIN);
        }
    }

    public void closeCafe(User authUser) {
        if (this.adminId.equals(authUser.getId())) {
            //todo : 사용 중인 seat도 종료, 이벤트 쓰는 것이 좋겠다.
            this.isOpen = false;
        } else {
            throw new CafeException(CafeErrorData.UNMATCHED_ADMIN);
        }
    }

    public void isAdminOrElseThrow(Long adminId) {
        if (!this.adminId.equals(adminId)) {
            throw new CafeException(CafeErrorData.CAFE_NEED_AUTH);
        }
    }
}

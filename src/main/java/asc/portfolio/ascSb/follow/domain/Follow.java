package asc.portfolio.ascSb.follow.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "FOLLOW")
public class Follow {

    @Id
    @Column(name = "FOLLOWER_ID")
    private Long followerId; // user

    @Column(name = "CAFE_ID")
    private Long cafeId;

    public Follow(Long followerId, Long cafeId) {
        this.followerId = followerId;
        this.cafeId = cafeId;
    }

    public void unfollow(Long cafeId) {
        if (this.cafeId.equals(cafeId)) {
            this.cafeId = null;
        }
    }

    public void changeFollowCafe(Long cafeId) {
        this.cafeId = cafeId;
    }
}

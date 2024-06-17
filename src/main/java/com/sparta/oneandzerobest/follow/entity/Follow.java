package com.sparta.oneandzerobest.follow.entity;

import com.sparta.oneandzerobest.timestamp.TimeStamp;
import jakarta.persistence.*;
import com.sparta.oneandzerobest.auth.entity.User;
import lombok.NoArgsConstructor;

/**
 * Follow 엔티티는 팔로우 관계를 나타내는 엔티티입니다.
 */
@Entity
@NoArgsConstructor
public class Follow extends TimeStamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "follower_id", nullable = false)
    private User follower; // 팔로우를 한 사용자

    @ManyToOne
    @JoinColumn(name = "followee_id", nullable = false)
    private User followee; // 팔로우를 당한 사용자

    public Follow(User follower1, User followee) {
        this.follower = follower1;
        this.followee = followee;
    }

    // Getter, Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowee() {
        return followee;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }
}
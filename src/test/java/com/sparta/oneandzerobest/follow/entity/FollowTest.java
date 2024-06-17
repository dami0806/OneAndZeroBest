package com.sparta.oneandzerobest.follow.entity;

import com.sparta.oneandzerobest.auth.entity.User;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FollowTest {

    /**
     * 전체 필드를 사용하는 생성자와 Getter 메서드를 테스트
     * 이 테스트는 생성자가 필드를 올바르게 초기화하는지 확인
     */
    @Test
    void 전체_필드_생성자와_Getter_테스트() {
        // given: 테스트 데이터를 준비
        User 팔로워 = new User();
        User 팔로위 = new User();

        // when: Follow 객체를 생성
        Follow follow = new Follow(팔로워, 팔로위);

        // then: 각 필드가 올바르게 초기화되었는지 확인
        assertEquals(팔로워, follow.getFollower());
        assertEquals(팔로위, follow.getFollowee());
    }

    /**
     * Setter 메서드를 테스트
     * 이 테스트는 Setter 메서드가 필드를 올바르게 설정하는지 확인
     */
    @Test
    void Setter_메서드_테스트() {
        // given: 테스트 데이터를 준비
        User 팔로워 = new User();
        User 팔로위 = new User();
        Follow follow = new Follow(null, null);

        // when: Setter 메서드를 호출하여 필드를 설정
        follow.setFollower(팔로워);
        follow.setFollowee(팔로위);

        // then: 각 필드가 올바르게 설정되었는지 확인
        assertEquals(팔로워, follow.getFollower());
        assertEquals(팔로위, follow.getFollowee());
    }

    /**
     * Getter 메서드를 테스트
     * 이 테스트는 Getter 메서드가 필드를 올바르게 반환하는지 확인
     */
    @Test
    void Getter_메서드_테스트() {
        // given: Follow 객체를 생성
        User 팔로워 = new User();
        User 팔로위 = new User();
        Follow follow = new Follow(팔로워, 팔로위);

        // when: Getter 메서드를 호출
        User 팔로워_결과 = follow.getFollower();
        User 팔로위_결과 = follow.getFollowee();

        // then: 각 Getter 메서드가 올바르게 값을 반환하는지 확인
        assertEquals(팔로워, 팔로워_결과);
        assertEquals(팔로위, 팔로위_결과);
    }

    /**
     * 예외 상황을 테스트
     * 예를 들어, 필드 값이 null일 때의 동작을 확인
     */
    @Test
    void null_값_테스트() {
        // given: null 값을 사용해서 Follow 객체를 생성
        Follow follow = new Follow(null, null);

        // then: 각 필드가 null로 초기화되었는지 확인
        assertNull(follow.getFollower());
        assertNull(follow.getFollowee());
    }
}

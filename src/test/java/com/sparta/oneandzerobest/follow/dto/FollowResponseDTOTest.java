package com.sparta.oneandzerobest.follow.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class FollowResponseDTOTest {

    @Test
    void 전체_필드_생성자와_Getter_테스트() {
        // given: 테스트 데이터를 준비
        Long 팔로워ID = 1L;
        Long 팔로위ID = 2L;
        String 메시지 = "팔로우 성공";

        // when: FollowResponseDTO 객체를 생성
        FollowResponseDTO dto = new FollowResponseDTO(팔로워ID, 팔로위ID, 메시지);

        // then: 각 필드가 올바르게 초기화되었는지 확인
        assertEquals(팔로워ID, dto.getFollowerId());
        assertEquals(팔로위ID, dto.getFolloweeId());
        assertEquals(메시지, dto.getMessage());
    }

    /**
     * followerId와 message만을 사용하는 생성자와 Getter 메서드를 테스트하기
     * 생성자가 필드를 올바르게 초기화하는지 확인
     */
    @Test
    void 팔로워ID와_메시지_생성자와_Getter_테스트() {
        // given: 테스트 데이터를 준비
        Long 팔로워ID = 1L;
        String 메시지 = "팔로우 성공";

        // when: FollowResponseDTO 객체를 생성
        FollowResponseDTO dto = new FollowResponseDTO(팔로워ID, 메시지);

        // then: 각 필드가 올바르게 초기화되었는지 확인
        assertEquals(팔로워ID, dto.getFollowerId());
        assertNull(dto.getFolloweeId()); // followeeId는 null이어야 한다.
        assertEquals(메시지, dto.getMessage());
    }

    /**
     * 예외 상황을 테스트.
     * 예를 들어, null 값이 들어갈 때의 동작을 확인
     */
    @Test
    void null_값_테스트() {
        // given: null 값을 사용해서 FollowResponseDTO 객체를 생성
        FollowResponseDTO dto = new FollowResponseDTO(null, null, null);

        // then: 각 필드가 null로 초기화되었는지 확인하기
        assertNull(dto.getFollowerId());
        assertNull(dto.getFolloweeId());
        assertNull(dto.getMessage());
    }
}

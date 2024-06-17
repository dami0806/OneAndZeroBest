package com.sparta.oneandzerobest.follow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FollowRequestDTOTest {

    @Test
    void testFollowRequestDTO() {
        // Test: 생성자를 통해 객체가 올바르게 생성되는지 확인
        // 생성자를 사용해서 FollowRequestDTO 객체를 생성
        Long followeeId = 1L;
        FollowRequestDTO followRequestDTO = new FollowRequestDTO(followeeId);

        // assertNotNull로 객체가 null이 아닌지 확인
        assertNotNull(followRequestDTO, "FollowRequestDTO 객체가 null입니다.");

        // assertEquals로 생성자를 통해 설정된 followeeId 값이 올바른지 확인하기
        assertEquals(followeeId, followRequestDTO.getFolloweeId(), "FolloweeId 값이 올바르지 않습니다.");
    }
}
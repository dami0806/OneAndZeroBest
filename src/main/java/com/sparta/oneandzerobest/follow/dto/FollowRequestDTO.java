package com.sparta.oneandzerobest.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * FollowRequestDTO는 팔로우 요청 시 필요한 데이터를 담는 DTO입니다.
 */
// 1. 기본 생성자를 통해 객체가 올바르게 생성되는지 테스트
// 2. Getter 메서드가 올바르게 동작하는지 테스트
@Getter
@AllArgsConstructor
public class FollowRequestDTO {
    private Long followeeId; // 팔로우할 사용자의 ID
}
package com.sparta.oneandzerobest.follow.dto;

import lombok.Getter;

/**
 * FollowResponseDTO는 팔로우 응답 시 반환되는 데이터를 담은 DTO입니다.
 */
@Getter
public class FollowResponseDTO {
    private Long followerId; // 팔로우를 하는 사용자의 ID
    private Long followeeId; // 팔로우 당하는 사용자의 ID
    private String message;  // 응답 메시지

    //followerId, followeeId, message가 올바르게 설정되는지 확인
    // 각 필드의 Getter 메서드가 올바르게 값을 반환하는지 확인
    public FollowResponseDTO(Long followerId, Long followeeId, String message) {
        this.followerId = followerId;
        this.followeeId = followeeId;
        this.message = message;
    }


//    followerId와 message가 올바르게 설정되는지 확인
//    followeeId가 null로 설정되는지 확인
//     각 필드의 Getter 메서드가 올바르게 값을 반환하는지 확인

    public FollowResponseDTO(Long followerId, String message) {
        this.followerId = followerId;
        this.message = message;
    }
}

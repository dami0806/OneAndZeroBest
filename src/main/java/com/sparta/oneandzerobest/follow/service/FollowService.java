package com.sparta.oneandzerobest.follow.service;

import com.sparta.oneandzerobest.follow.dto.FollowResponseDTO;
import com.sparta.oneandzerobest.follow.entity.Follow;
import com.sparta.oneandzerobest.auth.entity.User;
import com.sparta.oneandzerobest.follow.repository.FollowRepository;
import com.sparta.oneandzerobest.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * FollowService는 팔로우와 언팔로우 기능을 제공하는 서비스입니다.
 * 이 서비스는 사용자 간의 팔로우 관계를 관리합니다.
 */
@Service
public class FollowService {

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 팔로우 기능을 수행합니다.
     * @param followerId 팔로우하는 사용자의 ID
     * @param followeeId 팔로우 당하는 사용자의 ID
     * @return 팔로우 응답 데이터를 담은 DTO
     */
    public FollowResponseDTO follow(Long followerId, Long followeeId) {
        // Test: followerId로 사용자 조회
        // Mockito를 사용해서 userRepository.findById가 올바른 User 객체를 반환하는지 테스트
        // 테스트 코드: when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));

        // 팔로우하는 사용자 조회
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("팔로워를 찾을 수 없습니다."));

        // Test: followeeId로 사용자 조회
        // Mockito를 사용해서 userRepository.findById가 올바른 User 객체를 반환하는지 테스트
        // 테스트 코드: when(userRepository.findById(followeeId)).thenReturn(Optional.of(followee));

        // 팔로우 당하는 사용자 조회
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new RuntimeException("팔로위를 찾을 수 없습니다."));

        // Test: 이미 팔로우한 경우 예외 발생
        // Mockito를 사용해서 followRepository.findByFollowerAndFollowee가 값을 반환하는지 테스트
        // 테스트 코드: when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.of(follow));

        // 이미 팔로우한 경우 예외 발생
        if (followRepository.findByFollowerAndFollowee(follower, followee).isPresent()) {
            throw new RuntimeException("이미 팔로우한 유저입니다.");
        }

        // Test: 새로운 팔로우 관계 생성 및 저장
        // Mockito를 사용해서 followRepository.save가 올바르게 호출되는지 테스트
        // 테스트 코드: when(followRepository.save(any(Follow.class))).thenReturn(follow);

        // 새로운 팔로우 관계 생성 및 저장
        Follow follow = new Follow();
        follow.setFollower(follower);
        follow.setFollowee(followee);
        followRepository.save(follow);

        // Test: ResponseDTO 생성 및 반환
        // FollowResponseDTO가 잘 생성되고 반환되는지 테스트
        // 테스트 코드: FollowResponseDTO responseDTO = followService.follow(followerId, followeeId);
        // 테스트 코드: assertEquals(followerId, responseDTO.getFollowerId());
        // 테스트 코드: assertEquals(followeeId, responseDTO.getFolloweeId());
        // 테스트 코드: assertEquals("유저를 성공적으로 팔로우 했습니다.", responseDTO.getMessage());

        // ResponseDTO 생성 및 반환
        FollowResponseDTO responseDTO = new FollowResponseDTO(follower.getId(), followee.getId(), "유저를 성공적으로 팔로우 했습니다.");
        return responseDTO;
    }

    /**
     * 언팔로우 기능을 수행합니다.
     * @param followerId 언팔로우하는 사용자의 ID
     * @param followeeId 언팔로우 당하는 사용자의 ID
     * @return 언팔로우 응답 데이터를 담은 DTO
     */
    public FollowResponseDTO unfollow(Long followerId, Long followeeId) {
        // Test: followerId로 사용자 조회
        // Mockito를 사용해서 userRepository.findById가 올바른 User 객체를 반환하는지 테스트
        // 테스트 코드: when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));

        // 언팔로우하는 사용자 조회
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new RuntimeException("팔로워를 찾을 수 없습니다."));

        // Test: followeeId로 사용자 조회
        // Mockito를 사용해서 userRepository.findById가 올바른 User 객체를 반환하는지 테스트
        // 테스트 코드: when(userRepository.findById(followeeId)).thenReturn(Optional.of(followee));

        // 언팔로우 당하는 사용자 조회
        User followee = userRepository.findById(followeeId)
                .orElseThrow(() -> new RuntimeException("팔로위를 찾을 수 없습니다."));

        // Test: 팔로우 관계가 존재하지 않으면 예외 발생
        // Mockito를 사용해서 followRepository.findByFollowerAndFollowee가 값을 반환하는지 테스트
        // 테스트 코드: when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.of(follow));

        // 팔로우 관계가 존재하지 않으면 예외 발생
        Follow follow = followRepository.findByFollowerAndFollowee(follower, followee)
                .orElseThrow(() -> new RuntimeException("팔로우하지 않은 유저를 언팔로우 할 수 없습니다."));

        // Test: 팔로우 관계 삭제
        // Mockito를 사용해서 followRepository.delete가 올바르게 호출되는지 테스트
        // 테스트 코드: doNothing().when(followRepository).delete(follow);

        // 팔로우 관계 삭제
        followRepository.delete(follow);

        // Test: ResponseDTO 생성 및 반환
        // FollowResponseDTO가 올바르게 생성되고 반환되는지 테스트
        // 테스트 코드: FollowResponseDTO responseDTO = followService.unfollow(followerId, followeeId);
        // 테스트 코드: assertEquals(followerId, responseDTO.getFollowerId());
        // 테스트 코드: assertEquals(followeeId, responseDTO.getFolloweeId());
        // 테스트 코드: assertEquals("유저를 성공적으로 언팔로우 했습니다.", responseDTO.getMessage());

        // ResponseDTO 생성 및 반환
        FollowResponseDTO responseDTO = new FollowResponseDTO(follower.getId(), followee.getId(), "유저를 성공적으로 언팔로우 했습니다.");
        return responseDTO;
    }

    /**
     * 특정 사용자의 팔로워 목록을 조회합니다.
     * @param userId 팔로우 당하는 사용자의 ID
     * @return 팔로워 목록
     */
    public List<User> getFollowers(Long userId) {
        // Test: userId로 사용자 조회
        // Mockito를 사용하여 userRepository.findById가 올바른 User 객체를 반환하는지 테스트
        // 테스트 코드: when(userRepository.findById(userId)).thenReturn(Optional.of(followee));

        // 팔로우 당하는 사용자 조회
        User followee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));

        // Test: 팔로우 관계에서 팔로워 목록 추출
        // Mockito를 사용하여 followRepository.findByFollowee가 올바른 Follow 목록을 반환하는지 테스트
        // 테스트 코드: when(followRepository.findByFollowee(followee)).thenReturn(follows);

        // 팔로우 관계에서 팔로워 목록 추출
        return followRepository.findByFollowee(followee)
                .stream()
                .map(Follow::getFollower)
                .collect(Collectors.toList());
        // Test: 팔로워 목록이 올바르게 반환되는지 테스트
        // 테스트 코드: List<User> followers = followService.getFollowers(userId);
        // 테스트 코드: assertEquals(expectedFollowers, followers);
    }
}

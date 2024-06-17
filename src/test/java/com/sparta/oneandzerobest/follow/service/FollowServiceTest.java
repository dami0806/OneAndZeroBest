package com.sparta.oneandzerobest.follow.service;

import com.sparta.oneandzerobest.auth.entity.User;
import com.sparta.oneandzerobest.auth.entity.UserStatus;
import com.sparta.oneandzerobest.auth.repository.UserRepository;
import com.sparta.oneandzerobest.follow.dto.FollowResponseDTO;
import com.sparta.oneandzerobest.follow.entity.Follow;
import com.sparta.oneandzerobest.follow.repository.FollowRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 팔로우_성공() {
        Long followerId = 1L;
        Long followeeId = 2L;

        User follower = new User(followerId, "follower", "followerPassword", "followerName", "follower@example.com", UserStatus.ACTIVE);
        User followee = new User(followeeId, "followee", "followeePassword", "followeeName", "followee@example.com", UserStatus.ACTIVE);

        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followeeId)).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.empty());

        FollowResponseDTO response = followService.follow(followerId, followeeId);

        assertEquals(followerId, response.getFollowerId());
        assertEquals(followeeId, response.getFolloweeId());
        assertEquals("유저를 성공적으로 팔로우 했습니다.", response.getMessage());
    }

    @Test
    void 팔로우_실패_이미_팔로우한_경우() {
        Long followerId = 1L;
        Long followeeId = 2L;

        User follower = new User(followerId, "follower", "followerPassword", "followerName", "follower@example.com", UserStatus.ACTIVE);
        User followee = new User(followeeId, "followee", "followeePassword", "followeeName", "followee@example.com", UserStatus.ACTIVE);

        Follow existingFollow = new Follow(follower, followee);

        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followeeId)).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.of(existingFollow));

        Exception exception = assertThrows(RuntimeException.class, () -> followService.follow(followerId, followeeId));
        assertEquals("이미 팔로우한 유저입니다.", exception.getMessage());
    }

    @Test
    void 언팔로우_성공() {
        Long followerId = 1L;
        Long followeeId = 2L;

        User follower = new User(followerId, "follower", "followerPassword", "followerName", "follower@example.com", UserStatus.ACTIVE);
        User followee = new User(followeeId, "followee", "followeePassword", "followeeName", "followee@example.com", UserStatus.ACTIVE);

        Follow existingFollow = new Follow(follower, followee);

        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followeeId)).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.of(existingFollow));

        FollowResponseDTO response = followService.unfollow(followerId, followeeId);

        assertEquals(followerId, response.getFollowerId());
        assertEquals(followeeId, response.getFolloweeId());
        assertEquals("유저를 성공적으로 언팔로우 했습니다.", response.getMessage());
    }

    @Test
    void 언팔로우_실패_팔로우_하지_않은_경우() {
        Long followerId = 1L;
        Long followeeId = 2L;

        User follower = new User(followerId, "follower", "followerPassword", "followerName", "follower@example.com", UserStatus.ACTIVE);
        User followee = new User(followeeId, "followee", "followeePassword", "followeeName", "followee@example.com", UserStatus.ACTIVE);

        when(userRepository.findById(followerId)).thenReturn(Optional.of(follower));
        when(userRepository.findById(followeeId)).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowerAndFollowee(follower, followee)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> followService.unfollow(followerId, followeeId));
        assertEquals("팔로우하지 않은 유저를 언팔로우 할 수 없습니다.", exception.getMessage());
    }

    @Test
    void 팔로워_목록_조회_성공() {
        Long userId = 2L;
        User followee = new User(userId, "followee", "followeePassword", "followeeName", "followee@example.com", UserStatus.ACTIVE);

        User follower1 = new User(1L, "follower1", "follower1Password", "follower1Name", "follower1@example.com", UserStatus.ACTIVE);
        User follower2 = new User(3L, "follower2", "follower2Password", "follower2Name", "follower2@example.com", UserStatus.ACTIVE);

        Follow follow1 = new Follow(follower1, followee);
        Follow follow2 = new Follow(follower2, followee);

        List<Follow> follows = new ArrayList<>();
        follows.add(follow1);
        follows.add(follow2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(followee));
        when(followRepository.findByFollowee(followee)).thenReturn(follows);

        List<User> followers = followService.getFollowers(userId);

        assertEquals(2, followers.size());
        assertEquals("follower1", followers.get(0).getUsername());
        assertEquals("follower2", followers.get(1).getUsername());
    }
}

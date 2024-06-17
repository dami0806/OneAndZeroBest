package com.sparta.oneandzerobest.follow.controller;

import com.sparta.oneandzerobest.auth.config.JwtConfig;
import com.sparta.oneandzerobest.auth.entity.User;
import com.sparta.oneandzerobest.auth.entity.UserStatus;
import com.sparta.oneandzerobest.auth.repository.UserRepository;
import com.sparta.oneandzerobest.auth.util.JwtUtil;
import com.sparta.oneandzerobest.follow.dto.FollowResponseDTO;
import com.sparta.oneandzerobest.follow.dto.ErrorResponseDTO;
import com.sparta.oneandzerobest.follow.service.FollowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Base64;
import java.util.Optional;
import java.util.List;
import java.security.SecureRandom;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FollowControllerTest {

    @Mock
    private FollowService followService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowController followController;

    private JwtUtil jwtUtil;

    private final String username = "username";
    private final Long userId = 2L;
    private final Long followerId = 1L;
    private final User user = new User(followerId, "follower", "followerPassword", "followerName", "follower@example.com", UserStatus.ACTIVE);

    private String token;

    @BeforeEach
    void setUp() {
        // Mockito 초기화
        MockitoAnnotations.openMocks(this);

        // 강력한 키 생성
        byte[] keyBytes = new byte[32];
        new SecureRandom().nextBytes(keyBytes);
        String secretKey = Base64.getEncoder().encodeToString(keyBytes);

        // JwtConfig 설정
        JwtConfig jwtConfig = new JwtConfig(secretKey, 1000 * 60 * 60, 1000 * 60 * 60 * 24 * 7);
        JwtUtil.init(jwtConfig);

        // 테스트용 토큰 생성
        token = "Bearer " + JwtUtil.createAccessToken(username);

        // UserRepository 설정
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
    }

    @Test
    void 팔로우_성공() {
        // given
        FollowResponseDTO followResponseDTO = new FollowResponseDTO(followerId, userId, "유저를 성공적으로 팔로우 했습니다.");
        when(followService.follow(followerId, userId)).thenReturn(followResponseDTO);

        // when
        ResponseEntity<?> response = followController.follow(token, userId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(followResponseDTO, response.getBody());
    }

    @Test
    void 팔로우_실패_스스로_팔로우() {
        // given
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // when
        ResponseEntity<?> response = followController.follow(token, followerId);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("스스로를 팔로우할 수 없습니다.", ((ErrorResponseDTO) response.getBody()).getError());
    }

    @Test
    void 언팔로우_성공() {
        // given
        FollowResponseDTO followResponseDTO = new FollowResponseDTO(followerId, userId, "유저를 성공적으로 언팔로우 했습니다.");
        when(followService.unfollow(followerId, userId)).thenReturn(followResponseDTO);

        // when
        ResponseEntity<?> response = followController.unfollow(token, userId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(followResponseDTO, response.getBody());
    }

    @Test
    void 언팔로우_실패_팔로우하지_않은_유저() {
        // given
        doThrow(new RuntimeException("팔로우하지 않은 유저를 언팔로우 할 수 없습니다.")).when(followService).unfollow(followerId, userId);

        // when
        ResponseEntity<?> response = followController.unfollow(token, userId);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("팔로우하지 않은 유저를 언팔로우 할 수 없습니다.", ((ErrorResponseDTO) response.getBody()).getError());
    }

    @Test
    void 팔로워_목록_조회_성공() {
        // given
        User follower1 = new User(1L, "follower1", "follower1Password", "follower1Name", "follower1@example.com", UserStatus.ACTIVE);
        User follower2 = new User(2L, "follower2", "follower2Password", "follower2Name", "follower2@example.com", UserStatus.ACTIVE);
        List<User> followers = List.of(follower1, follower2);
        List<FollowResponseDTO> followersDTO = List.of(
                new FollowResponseDTO(follower1.getId(), follower1.getUsername()),
                new FollowResponseDTO(follower2.getId(), follower2.getUsername())
        );
        when(followService.getFollowers(userId)).thenReturn(followers);

        // when
        ResponseEntity<?> response = followController.getFollowers(token, userId);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<FollowResponseDTO> actualFollowersDTO = (List<FollowResponseDTO>) response.getBody();
        assertNotNull(actualFollowersDTO);
        assertEquals(followersDTO.size(), actualFollowersDTO.size());

        for (int i = 0; i < followersDTO.size(); i++) {
            assertEquals(followersDTO.get(i).getFollowerId(), actualFollowersDTO.get(i).getFollowerId());
            assertEquals(followersDTO.get(i).getFolloweeId(), actualFollowersDTO.get(i).getFolloweeId());
            assertEquals(followersDTO.get(i).getMessage(), actualFollowersDTO.get(i).getMessage());
        }
    }
}

package com.sparta.oneandzerobest.auth.service;

import com.sparta.oneandzerobest.auth.email.service.EmailService;
import com.sparta.oneandzerobest.auth.entity.SignupRequest;
import com.sparta.oneandzerobest.auth.entity.User;
import com.sparta.oneandzerobest.auth.entity.UserStatus;
import com.sparta.oneandzerobest.auth.repository.UserRepository;
import com.sparta.oneandzerobest.auth.util.JwtUtil;
import com.sparta.oneandzerobest.exception.IdPatternException;
import com.sparta.oneandzerobest.exception.InfoNotCorrectedException;
import com.sparta.oneandzerobest.exception.PasswordPatternException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    // Mock 객체 선언
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @Mock
    private JwtUtil jwtUtil;

    // 실제 테스트할 서비스 클래스에 모의 객체 주입
    @InjectMocks
    private UserServiceImpl userService;

    // 각 테스트 실행 전에 Mock 객체 초기화
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // RedisTemplate의 opsForValue() 메서드가 valueOperations를 반환하도록 설정
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    // 아이디 패턴이 잘못된 경우를 테스트
    @Test
    void testSignupWithInvalidUsername() {
        // 잘못된 아이디를 가진 SignupRequest 객체 생성
        SignupRequest signupRequest = SignupRequest.builder()
                .username("short")
                .password("ValidPass123!")
                .email("test@example.com")
                .build();

        // IdPatternException이 발생하는지 확인
        assertThrows(IdPatternException.class, () -> userService.signup(signupRequest));
    }

    // 비밀번호 패턴이 잘못된 경우를 테스트
    @Test
    void testSignupWithInvalidPassword() {
        // 잘못된 비밀번호를 가진 SignupRequest 객체 생성
        SignupRequest signupRequest = SignupRequest.builder()
                .username("validusername")
                .password("short")
                .email("test@example.com")
                .build();

        // PasswordPatternException이 발생하는지 확인
        assertThrows(PasswordPatternException.class, () -> userService.signup(signupRequest));
    }

    // 이미 존재하는 사용자 이름으로 회원가입 시도하는 경우를 테스트
    @Test
    void testSignupWithExistingUsername() {
        // 이미 존재하는 사용자 이름을 가진 SignupRequest 객체 생성
        SignupRequest signupRequest = SignupRequest.builder()
                .username("validusername")
                .password("ValidPass123!")
                .email("test@example.com")
                .build();

        // 이미 존재하는 User 객체 생성
        User existingUser = new User("validusername", "encodedPassword", "Existing User", "existing@example.com", UserStatus.UNVERIFIED);

        // userRepository가 해당 사용자 이름으로 사용자를 찾을 경우 반환하도록 설정
        when(userRepository.findByUsername("validusername")).thenReturn(Optional.of(existingUser));

        // 회원가입 메서드 호출
        userService.signup(signupRequest);

        // 이메일 서비스가 한 번 호출되었는지 확인
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }

    // 이미 존재하는 이메일로 회원가입 시도하는 경우를 테스트
    @Test
    void testSignupWithExistingEmail() {
        // 이미 존재하는 이메일을 가진 SignupRequest 객체 생성
        SignupRequest signupRequest = SignupRequest.builder()
                .username("validusername")
                .password("ValidPass123!")
                .email("test@example.com")
                .build();

        // userRepository가 해당 이메일로 사용자를 찾을 경우 반환하도록 설정
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        // InfoNotCorrectedException이 발생하는지 확인
        assertThrows(InfoNotCorrectedException.class, () -> userService.signup(signupRequest));
    }

    // 성공적으로 회원가입하는 경우를 테스트
    @Test
    void testSignupSuccessful() {
        // 유효한 정보를 가진 SignupRequest 객체 생성
        SignupRequest signupRequest = SignupRequest.builder()
                .username("validusername")
                .password("ValidPass123!")
                .email("test@example.com")
                .build();

        // userRepository가 해당 사용자 이름과 이메일로 사용자를 찾지 못하도록 설정
        when(userRepository.findByUsername("validusername")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("ValidPass123!")).thenReturn("encodedPassword");

        // 회원가입 메서드 호출
        userService.signup(signupRequest);

        // userRepository가 한 번 사용자 정보를 저장했는지 확인
        verify(userRepository, times(1)).save(any(User.class));
        // 이메일 서비스가 한 번 호출되었는지 확인
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }
}

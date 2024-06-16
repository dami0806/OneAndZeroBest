package com.sparta.oneandzerobest.auth.entity;

import com.sparta.oneandzerobest.auth.repository.UserRepository;
import com.sparta.oneandzerobest.profile.dto.ProfileRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserTest {
    // 테스트용 엔티티 매니저, JPA 엔티티를 직접 관리
    @Autowired
    private TestEntityManager entityManager;

    // 자동으로 구성된 UserRepository 인스턴스
    @Autowired
    private UserRepository userRepository;

    // 초기 세팅
    @BeforeEach
    public void setup() {

    }

    @Test
    public void 사용자_생성_및_저장_테스트() {
        // Given
        User newUser = new User("다미", "비밀번호", "USER", "dami@naver.com", UserStatus.ACTIVE);

        // When
        entityManager.persist(newUser);
        entityManager.flush();
        User foundUser = entityManager.find(User.class, newUser.getId());

        // Then
        assertEquals("다미", foundUser.getUsername());
        assertEquals(newUser.getUsername(), foundUser.getUsername());
        assertEquals(newUser.getPassword(), foundUser.getPassword());

    }

    @Test
    public void 사용자_프로필_업데이트_테스트() {
        // Given: 기존 사용자 설정 및 프로필 업데이트 정보 준비
        User user = new User("다미", "비밀번호", "USER", "dami@naver.com", UserStatus.ACTIVE);
        entityManager.persistAndFlush(user);
        ProfileRequestDto profileDto = new ProfileRequestDto("Updated Name", "Updated Introduction", "비밀번호", "newPassword");

        // When: 사용자 프로필 업데이트 실행
        user.update(profileDto);
        entityManager.persistAndFlush(user);

        // Then: 업데이트된 정보가 데이터베이스에 반영되었는지 검증
        User updatedUser = userRepository.findById(user.getId()).orElseThrow();
        assertEquals("Updated Name", updatedUser.getName()); // 업데이트된 이름을 검증
        assertEquals("Updated Introduction", updatedUser.getIntroduction());
    }


    @Test
    public void 사용자_회원_탈퇴_테스트() {
        // Given: 새 사용자 등록
        User newUser = new User("다미", "비밀번호", "USER", "dami@naver.com", UserStatus.ACTIVE);
        entityManager.persistAndFlush(newUser);

        // When: 사용자 탈퇴 처리 실행
        newUser.withdraw();
        entityManager.persistAndFlush(newUser);

        // Then: 사용자 상태가 탈퇴로 변경되었는지 검증
        User withdrawnUser = userRepository.findById(newUser.getId()).orElseThrow();

        assertEquals(UserStatus.WITHDRAWN, withdrawnUser.getStatusCode());
        assertNull(withdrawnUser.getRefreshToken(), "탈퇴 시 리프레시 토큰은 삭제되어야 한다.");
    }
}
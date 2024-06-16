package com.sparta.oneandzerobest.auth.service;

import com.sparta.oneandzerobest.auth.entity.User;
import com.sparta.oneandzerobest.auth.entity.UserStatus;
import com.sparta.oneandzerobest.auth.repository.UserRepository;
import com.sparta.oneandzerobest.exception.InfoNotCorrectedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername_UserFound() {
        // Given
        User user = new User("dami", "password", "dami", "dami@example.com", UserStatus.ACTIVE);
        when(userRepository.findByUsername("dami")).thenReturn(Optional.of(user));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("dami");

        // Then
        assertNotNull(userDetails);
        assertEquals("dami", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UserNotFound() {
        // Given
        when(userRepository.findByUsername("다미")).thenReturn(Optional.empty());

        // When / Then
        Exception exception = assertThrows(InfoNotCorrectedException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistentuser");
        });

        String expectedMessage = "사용자를 찾을 수 없습니다.";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}

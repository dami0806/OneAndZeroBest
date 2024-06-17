package com.sparta.oneandzerobest.follow.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseDTOTest {

    // Test: 생성자를 통해 객체가 올바르게 생성되는지 확인
    // 생성자를 사용해서 ErrorResponseDTO 객체를 생성하고, 올바른 값으로 초기화되는지 테스트
    @Test
    void testErrorResponseDTO() {
        String errorMessage = "에러 메시지";
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(errorMessage);

        // assertNotNull을 사용해서 객체가 null이 아닌지 확인하기
        assertNotNull(errorResponseDTO, "ErrorResponseDTO 객체가 null입니다.");
        // assertEquals를 사용해서 error 필드가 올바르게 초기화되었는지 확인하기
        assertEquals(errorMessage, errorResponseDTO.getError(), "ErrorResponseDTO의 error 값이 올바르지 않습니다.");
    }

    // Test: getError() 메서드가 올바른 값을 반환하는지 확인
    // 생성자를 사용해서 ErrorResponseDTO 객체를 생성하고, getError() 메서드가 올바른 값을 반환하는지 테스트하기
    @Test
    void testGetError() {

        String errorMessage = "다른 에러 메시지";
        ErrorResponseDTO errorResponseDTO = new ErrorResponseDTO(errorMessage);

        // assertEquals를 사용해서 getError() 메서드가 올바른 값을 반환하는지 확인하기
        assertEquals(errorMessage, errorResponseDTO.getError(), "getError() 메서드가 올바른 값을 반환하지 않습니다.");
    }
}
package com.sparta.oneandzerobest.follow.dto;

import lombok.Getter;

/**
 * ErrorResponseDTO는 오류 응답 시 반환되는 데이터를 담는 DTO입니다.
 */

// 1. 생성자를 통해 객체가 올바르게 생성되는지 테스트
// 2. getError() 메서드가 올바른 값을 반환하는지 테스트

@Getter
public class ErrorResponseDTO {
    private String error;

    public ErrorResponseDTO(String error) {
        this.error = error;
    }
}
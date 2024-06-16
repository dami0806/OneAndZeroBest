package com.sparta.oneandzerobest.profile.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String name;
    private String introduction;
    private String password;
    private String newPassword;

    public ProfileRequestDto(String name, String introduction, String password, String newPassword) {
        this.name = name;
        this.introduction = introduction;
        this.password = password;
        this.newPassword = newPassword;
    }
}

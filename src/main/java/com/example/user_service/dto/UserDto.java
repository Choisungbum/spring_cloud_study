package com.example.user_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private String email;
    private String name;
    private String pwd;
    private String userId;
    private LocalDate createdAt;

    private String encryptedPwd;

    @Override
    public String toString() {
        return "UserDto{" +
                "email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", pwd='" + pwd + '\'' +
                ", userId='" + userId + '\'' +
                ", createdAt=" + createdAt +
                ", encryptedPwd='" + encryptedPwd + '\'' +
                '}';
    }
}

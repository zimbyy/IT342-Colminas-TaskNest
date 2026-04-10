package edu.cit.colminas.tasknest.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String message;
    private String username;
    private String firstName;
    private String lastName;
    private Long userId;

    public AuthResponse(String message, Long userId, String username, String firstName, String lastName) {
        this.message = message;
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
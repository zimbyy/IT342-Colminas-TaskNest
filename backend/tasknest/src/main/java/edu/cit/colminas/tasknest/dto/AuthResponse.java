package edu.cit.colminas.tasknest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String message;
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
}

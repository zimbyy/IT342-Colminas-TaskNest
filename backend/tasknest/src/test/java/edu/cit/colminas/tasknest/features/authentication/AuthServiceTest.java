package edu.cit.colminas.tasknest.features.authentication;

import edu.cit.colminas.tasknest.features.authentication.dto.LoginRequest;
import edu.cit.colminas.tasknest.features.authentication.dto.RegisterRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Test
    void loginRequest_fieldsSetCorrectly() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");
        assertEquals("testuser", request.getUsername());
        assertEquals("password123", request.getPassword());
    }

    @Test
    void registerRequest_fieldsSetCorrectly() {
        RegisterRequest request = new RegisterRequest();
        request.setEmail("new@example.com");
        request.setUsername("newuser");
        request.setPassword("secure123");
        request.setFirstName("Test");
        request.setLastName("User");
        assertEquals("new@example.com", request.getEmail());
        assertEquals("newuser", request.getUsername());
        assertEquals("Test", request.getFirstName());
    }
}

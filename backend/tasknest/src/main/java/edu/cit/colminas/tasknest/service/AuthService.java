package edu.cit.colminas.tasknest.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.cit.colminas.tasknest.dto.AuthResponse;
import edu.cit.colminas.tasknest.dto.LoginRequest;
import edu.cit.colminas.tasknest.dto.RegisterRequest;
import edu.cit.colminas.tasknest.model.User;
import edu.cit.colminas.tasknest.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public AuthResponse register(RegisterRequest request) {
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already registered");
        }

        // Confirm password should match password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Password and confirm password do not match");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        // Save to database
        User savedUser = userRepository.save(user);
        
        return new AuthResponse(
            "Registration successful",
            savedUser.getId(),
            savedUser.getUsername(),
            savedUser.getFirstName(),
            savedUser.getLastName()
        );
    }
    
    public AuthResponse login(LoginRequest request) {
        // Find user by username
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        
        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        
        return new AuthResponse(
            "Login successful",
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName()
        );
    }
}

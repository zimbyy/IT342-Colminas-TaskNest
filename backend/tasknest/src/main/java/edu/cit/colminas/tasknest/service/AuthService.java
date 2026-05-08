package edu.cit.colminas.tasknest.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.cit.colminas.tasknest.dto.AuthResponse;
import edu.cit.colminas.tasknest.dto.LoginRequest;
import edu.cit.colminas.tasknest.dto.RegisterRequest;
import edu.cit.colminas.tasknest.model.User;
import edu.cit.colminas.tasknest.repository.UserRepository;
import edu.cit.colminas.tasknest.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final SupabaseAuthService supabaseAuthService;
    
    public AuthResponse register(RegisterRequest request) {
        log.info("Starting registration for user: {}", request.getUsername());
        
        // Check if username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already registered");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        // Confirm password should match password
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Password and confirm password do not match");
        }
        
        String supabaseUserId = null;
        
        try {
            // Step 1: Create user in Supabase Auth
            // This will trigger the database trigger to create user in public.users
            supabaseUserId = supabaseAuthService.createUserInSupabase(
                request.getEmail(),
                request.getPassword(),
                request.getUsername(),
                request.getFirstName(),
                request.getLastName()
            );
            
            log.info("Successfully created user in Supabase Auth with ID: {}", supabaseUserId);
            
            // Step 2: Build user object directly from request data
            // We do NOT fetch from userRepository since Spring Boot uses its own DB
            // The Supabase trigger handles inserting into public.users automatically
            User user = new User();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setProviderId(supabaseUserId);
            
            // Step 3: Save user to Spring Boot's own database
            User savedUser = userRepository.save(user);
            
            log.info("Successfully saved user to local database with ID: {}", savedUser.getId());
            
            // Step 4: Generate JWT token
            String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getUsername());
            
            log.info("Registration completed successfully for user: {}", request.getUsername());
            
            return new AuthResponse(
                "Registration successful",
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                token
            );
            
        } catch (Exception e) {
            log.error("Registration failed for user: {}", request.getUsername(), e);
            
            // Cleanup: if Supabase user was created but local save failed,
            // delete the Supabase user to avoid orphaned auth records
            if (supabaseUserId != null) {
                try {
                    supabaseAuthService.deleteUserFromSupabase(supabaseUserId);
                    log.info("Cleaned up Supabase Auth user: {}", supabaseUserId);
                } catch (Exception cleanupError) {
                    log.error("Failed to cleanup Supabase Auth user: {}", supabaseUserId, cleanupError);
                }
            }
            
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }
    
    public AuthResponse login(LoginRequest request) {
        log.info("Attempting login for username: {}", request.getUsername());

        // Find user by username in local database
        User user = userRepository.findByUsername(request.getUsername())
            .orElseThrow(() -> {
                log.error("User not found: {}", request.getUsername());
                return new RuntimeException("Invalid username or password");
            });

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            log.error("Password mismatch for username: {}", request.getUsername());
            throw new RuntimeException("Invalid username or password");
        }

        log.info("Login successful for username: {}", request.getUsername());
        
        // Generate JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        return new AuthResponse(
            "Login successful",
            user.getId(),
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            token
        );
    }
}
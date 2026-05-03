package edu.cit.colminas.tasknest.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.cit.colminas.tasknest.dto.PasswordChangeRequest;
import edu.cit.colminas.tasknest.dto.ProfileUpdateRequest;
import edu.cit.colminas.tasknest.model.User;
import edu.cit.colminas.tasknest.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User getProfile(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public User updateProfile(Long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        return userRepository.save(user);
    }
    
    public void changePassword(Long userId, PasswordChangeRequest request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }
        
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }
        
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

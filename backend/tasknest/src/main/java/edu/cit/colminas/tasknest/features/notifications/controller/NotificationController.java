package edu.cit.colminas.tasknest.features.notifications.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.cit.colminas.tasknest.shared.dto.ApiResponse;
import edu.cit.colminas.tasknest.features.authentication.repository.UserRepository;
import edu.cit.colminas.tasknest.features.notifications.model.Notification;
import edu.cit.colminas.tasknest.features.notifications.dto.NotificationRequest;
import edu.cit.colminas.tasknest.features.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    
    private final NotificationService notificationService;
    private final UserRepository userRepository;
    
    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserNotifications(
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<Notification> notifications = notificationService.getUserNotifications(String.valueOf(user.getId()));
            
            Map<String, Object> response = new HashMap<>();
            response.put("notifications", notifications);
            response.put("unreadCount", notificationService.getUnreadNotifications(String.valueOf(user.getId())).size());
            
            return ResponseEntity.ok(ApiResponse.success(response));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/unread")
    public ResponseEntity<ApiResponse<List<Notification>>> getUnreadNotifications(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            List<Notification> notifications = notificationService.getUnreadNotifications(String.valueOf(user.getId()));
            
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Notification>> createNotification(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody NotificationRequest request) {
        try {
            var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            String message = request.getMessage();
            String type = request.getType();
            
            Notification notification = notificationService.createNotification(String.valueOf(user.getId()), message, type);
            
            return ResponseEntity.ok(ApiResponse.success(notification));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/read/{notificationId}")
    public ResponseEntity<ApiResponse<String>> markAsRead(@PathVariable String notificationId) {
        try {
            notificationService.markAsRead(notificationId);
            return ResponseEntity.ok(ApiResponse.success("Notification marked as read"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/read-all")
    public ResponseEntity<ApiResponse<String>> markAllAsRead(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
            
            notificationService.markAllAsRead(String.valueOf(user.getId()));
            return ResponseEntity.ok(ApiResponse.success("All notifications marked as read"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
}

package edu.cit.colminas.tasknest.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SupabaseAuthService {
    
    @Value("${supabase.url}")
    private String supabaseUrl;
    
    @Value("${supabase.service-key}")
    private String supabaseServiceKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    /**
     * Create a user in Supabase Auth
     */
    public String createUserInSupabase(String email, String password, String username, String firstName, String lastName) {
        try {
            String url = supabaseUrl + "/auth/v1/admin/users";
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseServiceKey);
            headers.set("apikey", supabaseServiceKey);
            headers.set("Content-Type", "application/json");
            
            // Create user metadata
            String userMetadata = String.format(
                "{\"username\":\"%s\",\"first_name\":\"%s\",\"last_name\":\"%s\"}",
                username, firstName, lastName
            );
            
            // Request body
            String requestBody = String.format(
                "{\"email\":\"%s\",\"password\":\"%s\",\"email_confirm\":true,\"user_metadata\":%s}",
                email, password, userMetadata
            );
            
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            log.info("Creating user in Supabase Auth: {}", email);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode responseJson = objectMapper.readTree(response.getBody());
                String userId = responseJson.get("id").asText();
                log.info("Successfully created user in Supabase Auth with ID: {}", userId);
                return userId;
            } else {
                log.error("Failed to create user in Supabase Auth: {}", response.getStatusCode());
                throw new RuntimeException("Failed to create user in Supabase Auth");
            }
            
        } catch (Exception e) {
            log.error("Error creating user in Supabase Auth", e);
            throw new RuntimeException("Error creating user in Supabase Auth: " + e.getMessage());
        }
    }
    
    /**
     * Delete a user from Supabase Auth
     */
    public void deleteUserFromSupabase(String userId) {
        try {
            String url = supabaseUrl + "/auth/v1/admin/users/" + userId;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + supabaseServiceKey);
            headers.set("apikey", supabaseServiceKey);
            
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            log.info("Deleting user from Supabase Auth: {}", userId);
            
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.DELETE, entity, String.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully deleted user from Supabase Auth: {}", userId);
            } else {
                log.error("Failed to delete user from Supabase Auth: {}", response.getStatusCode());
            }
            
        } catch (Exception e) {
            log.error("Error deleting user from Supabase Auth", e);
        }
    }
}

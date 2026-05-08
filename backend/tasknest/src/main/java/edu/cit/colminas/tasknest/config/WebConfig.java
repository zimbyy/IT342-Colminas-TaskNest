package edu.cit.colminas.tasknest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebConfig {
    // CORS is handled by SecurityConfig
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
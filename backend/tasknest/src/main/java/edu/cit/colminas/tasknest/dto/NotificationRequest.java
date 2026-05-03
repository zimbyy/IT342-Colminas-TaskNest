package edu.cit.colminas.tasknest.dto;

import lombok.Data;

@Data
public class NotificationRequest {
    private String message;
    private String type;

    public NotificationRequest() {}

    public NotificationRequest(String message, String type) {
        this.message = message;
        this.type = type;
    }
}

package edu.cit.colminas.tasknest.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private ErrorInfo error;
    private String timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, data, null, LocalDateTime.now().toString());
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, data, null, LocalDateTime.now().toString());
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, null, new ErrorInfo("GENERIC_ERROR", message), LocalDateTime.now().toString());
    }

    public static <T> ApiResponse<T> error(String code, String message) {
        return new ApiResponse<>(false, null, new ErrorInfo(code, message), LocalDateTime.now().toString());
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorInfo {
        private String code;
        private String message;
    }
}

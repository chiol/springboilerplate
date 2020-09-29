package kr.ibct.springboilerplate.common;


import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int code;
    private String error;
    private String message;
    private String path;

    @Builder
    public ErrorResponse(int code, String message, String error, String path) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.error = error;
        this.path = path;
    }
}

package kr.ibct.springboilerplate.commons;

import lombok.Data;

@Data
public class ApiResponse {
    private final boolean success;
    private final String message;
}

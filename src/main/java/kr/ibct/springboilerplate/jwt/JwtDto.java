package kr.ibct.springboilerplate.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;

public class JwtDto {

    @Data
    @AllArgsConstructor
    public static class AccessTokenAndRefreshToken {
        private String accessToken;
        private String refreshToken;
    }
}

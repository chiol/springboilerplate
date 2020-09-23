package kr.ibct.springboilerplate.jwt;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

@Data
public class JwtTokenResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    @Value("${app.jwtExpirationInMs}")
    private int expires_in;


    public JwtTokenResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}

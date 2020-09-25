package kr.ibct.springboilerplate.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenResponse {

    private String tokenType = "Bearer";
    private String accessToken;
    private int accessTokenExpiresInDay;
    private String refreshToken;
    private int refreshTokenExpiresInDay;

    public JwtTokenResponse(String accessToken, int accessTokenExpiresInDay,
            String refreshToken, int refreshTokenExpiresInDay) {
        this.accessToken = accessToken;
        this.accessTokenExpiresInDay = accessTokenExpiresInDay;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresInDay = refreshTokenExpiresInDay;
    }
}

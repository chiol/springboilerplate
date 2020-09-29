package kr.ibct.springboilerplate.jwt;

import io.jsonwebtoken.*;
import kr.ibct.springboilerplate.account.AccountAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
@Slf4j
public class JwtTokenProvider {

    @Value("${jwt.secretKey}")
    private String jwtSecretKey;

    @Value("${jwt.accessToken.expiresInDay}")
    private int accessTokenExpiresInDay;

    @Value("${jwt.refreshToken.expiresInDay}")
    private int refreshTokenExpiresInDay;


    public String generateAccessToken(Authentication authentication) {
        AccountAdapter accountAdapter = (AccountAdapter) authentication.getPrincipal();

        // Todo accessToken이 바로 다시 생성하면 변경되지 않음.
        Date now = new Date(System.currentTimeMillis());
        Date expired = addDay(now, accessTokenExpiresInDay);

        return Jwts.builder()
                .setSubject(Long.toString(accountAdapter.getAccount().getId()))
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey).compact();
    }


    public String generateRefreshToken(Authentication authentication) {
        AccountAdapter accountAdapter = (AccountAdapter) authentication.getPrincipal();

        Date now = new Date();
        Date expired = addDay(now, refreshTokenExpiresInDay);
        return Jwts.builder()
                .setSubject(Long.toString(accountAdapter.getAccount().getId()))
                .setIssuedAt(now)
                .setExpiration(expired)
                .signWith(SignatureAlgorithm.HS512, jwtSecretKey).compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }


    public Long getUserIdFromJwt(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    private Date addDay(Date now, int day) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, day);
        return c.getTime();
    }

}

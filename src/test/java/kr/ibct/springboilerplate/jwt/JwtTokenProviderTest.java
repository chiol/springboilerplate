package kr.ibct.springboilerplate.jwt;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountAdapter;
import kr.ibct.springboilerplate.account.AccountRole;
import kr.ibct.springboilerplate.account.AccountService;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtTokenProviderTest extends BaseTest {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void testGenerateToken() {

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
        Authentication authentication = authenticationManager.authenticate(token);
        String s = jwtTokenProvider.generateAccessToken(authentication);

        assertThat(jwtTokenProvider.validateToken(s)).isTrue();
        assertThat(jwtTokenProvider.getUserIdFromJwt(s)).isEqualTo(2L);
        System.out.println(jwtTokenProvider.getUserIdFromJwt(s));
    }
}
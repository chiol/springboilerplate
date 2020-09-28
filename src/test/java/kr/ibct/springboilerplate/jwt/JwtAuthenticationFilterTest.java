package kr.ibct.springboilerplate.jwt;

import kr.ibct.springboilerplate.account.AccountService;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationFilterTest extends BaseTest {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Test
    public void testJwtAuthenticationFilter() throws Exception {
        Authentication admin = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminEmail, adminPassword)
        );
        String adminToken = jwtTokenProvider.generateAccessToken(admin);

        Authentication user = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, userPassword)
        );
        String userToken = jwtTokenProvider.generateAccessToken(user);

        String errorToken = "errorToken";

        this.mockMvc
                .perform(get("/auth")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("testOk"));

        this.mockMvc
                .perform(get("/auth")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ errorToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        this.mockMvc
                .perform(get("/auth/admin")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("testOk"));

        this.mockMvc
                .perform(get("/auth/admin")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer "+ userToken))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
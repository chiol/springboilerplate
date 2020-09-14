package kr.ibct.springboilerplate.jwt;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountRole;
import kr.ibct.springboilerplate.account.AccountService;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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
        String adminToken = jwtTokenProvider.generateToken(admin);

        Authentication user = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, userPassword)
        );
        String userToken = jwtTokenProvider.generateToken(user);

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
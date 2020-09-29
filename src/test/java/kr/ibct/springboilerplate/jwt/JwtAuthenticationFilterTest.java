package kr.ibct.springboilerplate.jwt;

import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class JwtAuthenticationFilterTest extends BaseTest {

    @Test
    public void testJwtAuthenticationFilter() throws Exception {
        createUserAccount();
        createAdminAccount();

        String adminToken = getAdminAccessToken();
        String userToken = getUserAccessToken();
        String errorToken = "errorToken";

        this.mockMvc
                .perform(get("/auth")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("testOk"));

        this.mockMvc
                .perform(get("/auth")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + errorToken))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        this.mockMvc
                .perform(get("/auth/admin")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("testOk"));

        this.mockMvc
                .perform(get("/auth/admin")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken))
                .andDo(print())
                .andExpect(status().isForbidden());

    }
}
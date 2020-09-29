package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Todo REST_DOCS 적용하기
@ExtendWith(RestDocumentationExtension.class)
class AccountControllerTestByUnauth extends BaseTest {

    String BASE_URL = "/api/v1/users";

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity()) // security 추가
                .build();
    }

    @Test
    public void testSignUp() throws Exception {
        // given
        AccountDto.SignUpRequest request = new AccountDto.SignUpRequest();
        request.setEmail("test@Test.com");
        request.setPassword("testPassword");

        this.mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("signUp"));

        this.mockMvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSignIn() throws Exception {
        // given not account
        AccountDto.SignInRequest request = new AccountDto.SignInRequest();
        request.setEmail("not@exist.account");
        request.setPassword("password");

        this.mockMvc.perform(post(BASE_URL + "/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGet() throws Exception {
        this.mockMvc.perform(get(BASE_URL + "/" + 1))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPatch() throws Exception {

        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setUsername("newUsername");

        this.mockMvc.perform(
                patch(BASE_URL + "/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testPut() throws Exception {

        AccountDto.PutRequest request = new AccountDto.PutRequest();
        request.setUsername("newUsername");
        request.setPhoneNum("newPhoneNum");
        request.setAddress("newAddress");

        this.mockMvc.perform(
                put(BASE_URL + "/" + 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

    @Test
    public void testDelete() throws Exception {

        this.mockMvc.perform(
                delete(BASE_URL + "/" + 1))
                .andDo(print())
                .andExpect(status().isUnauthorized());

    }

}
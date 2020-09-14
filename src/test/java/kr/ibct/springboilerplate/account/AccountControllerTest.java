package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Todo REST_DOCS 적용하기
@ExtendWith(RestDocumentationExtension.class)
class AccountControllerTest extends BaseTest {

    String BASE_URL = "/api/v1/users";
    @Autowired
    AccountRepository accountRepository;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    @Order(1)
    public void testSignUp() throws Exception {
        AccountDto.signUpRequest request = new AccountDto.signUpRequest();
        request.setEmail("test@Test.com");
        request.setPassword("testPassword");
        this.mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("signUp"));
    }
    @Test
    @Order(2)
    public void testGet() throws Exception {
        Account account = accountRepository.findByEmail(userEmail).orElseThrow();
        this.mockMvc.perform(get(BASE_URL + "/" + account.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("get"));
    }

    @Test
    @Order(2)
    public void testUpdate() throws Exception {

        Account account = accountRepository.findByEmail(userEmail).orElseThrow();

        AccountDto.updateRequest request = new AccountDto.updateRequest();
        request.setPassword("123");

        this.mockMvc.perform(patch(BASE_URL+"/"+account.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update"));
        this.mockMvc.perform(patch(BASE_URL+"/"+"124512")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(3)
    public void testDelete() throws Exception {
        Account account = accountRepository.findByEmail(userEmail).orElseThrow();

        this.mockMvc.perform(delete(BASE_URL+"/"+account.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete"));

        this.mockMvc.perform(delete(BASE_URL+"/"+"123123"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

}
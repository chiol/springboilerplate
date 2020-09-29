package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Todo REST_DOCS 적용하기
@ExtendWith(RestDocumentationExtension.class)
class AccountControllerTestByAdmin extends BaseTest {

    String BASE_URL = "/api/v1/users";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        createAccounts();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(documentationConfiguration(restDocumentation))
                .apply(springSecurity()) // security 추가
                .build();
    }


    @Test
    public void testGet() throws Exception {

        String token = getTokenAndCreateAdminAccount();

        List<Account> allAccount = accountRepository.findAll();

        for (Account account : allAccount) {
            this.mockMvc.perform(get(BASE_URL + "/" + account.getId())
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("admin/get"));
        }

    }

    @Test
    public void testGetAll() throws Exception {
        String token = getTokenAndCreateAdminAccount();


        this.mockMvc.perform(get(BASE_URL)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("admin/getAll"));

    }

    @Test
    public void testPatch() throws Exception {
        String token = getTokenAndCreateAdminAccount();

        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setAddress("newAddress");
        request.setPhoneNum("newPhoneNum");
        request.setUsername("newUsername");

        List<Account> allAccount = accountRepository.findAll();

        for (Account account : allAccount) {

            this.mockMvc.perform(patch(BASE_URL + "/" + account.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("admin/patch"));
        }

    }


    @Test
    public void testPut() throws Exception {
        String token = getTokenAndCreateAdminAccount();

        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setAddress("newAddress");
        request.setPhoneNum("newPhoneNum");
        request.setUsername("newUsername");

        List<Account> allAccount = accountRepository.findAll();

        for (Account account : allAccount) {

            this.mockMvc.perform(patch(BASE_URL + "/" + account.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("admin/put"));
        }

    }


    @Test
    public void testDelete() throws Exception {
        String token = getTokenAndCreateAdminAccount();

        List<Account> allAccount = accountRepository.findAll();

        for (Account account : allAccount) {
            this.mockMvc
                    .perform(delete(BASE_URL + "/" + account.getId())
                            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("admin/delete"));
        }
    }

    private String getTokenAndCreateAdminAccount() {
        createAdminAccount();
        return getAdminAccessToken();
    }

}
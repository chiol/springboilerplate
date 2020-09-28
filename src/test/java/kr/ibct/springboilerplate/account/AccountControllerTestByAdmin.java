package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.common.BaseTest;
import kr.ibct.springboilerplate.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Account createAdminAccount() {
        Account account = Account.builder()
                .email(adminEmail)
                .password(adminPassword)
                .roles(Set.of(AccountRole.USER,AccountRole.ADMIN))
                .build();
        accountService.save(account);
        return account;
    }

    public void createAccounts() {
        int total = 10;
        List<Account> accounts = new ArrayList<>();
        for (int i = 0; i < total; i++) {
            Account account = Account.builder()
                    .email(userEmail + i)
                    .password(adminPassword + i)
                    .roles(Set.of(AccountRole.USER,AccountRole.ADMIN))
                    .build();
            accounts.add(account);
        }
        accountRepository.saveAll(accounts);
    }

    public String getToken() {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminEmail,adminPassword)
        );
        return jwtTokenProvider.generateAccessToken(authentication);
    }


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        accountRepository.deleteAll();
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
        createAdminAccount();
        String token = getToken();

        List<Account> allAccount = accountRepository.findAll();

        for (Account account: allAccount) {
            this.mockMvc.perform(get(BASE_URL + "/" + account.getId())
                    .header(HttpHeaders.AUTHORIZATION,"Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("admin/get"));
        }

    }

    @Test
    public void testGetAll() throws Exception {
        createAdminAccount();
        String token = getToken();


        this.mockMvc.perform(get(BASE_URL )
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("admin/getAll"));

    }

    @Test
    public void testPatch() throws Exception {
        createAdminAccount();
        String token = getToken();

        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setAddress("newAddress");
        request.setPhoneNum("newPhoneNum");
        request.setUsername("newUsername");

        List<Account> allAccount = accountRepository.findAll();

        for (Account account: allAccount) {

            this.mockMvc.perform(patch(BASE_URL+"/"+account.getId())
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
        createAdminAccount();
        String token = getToken();

        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setAddress("newAddress");
        request.setPhoneNum("newPhoneNum");
        request.setUsername("newUsername");

        List<Account> allAccount = accountRepository.findAll();

        for (Account account: allAccount) {

            this.mockMvc.perform(patch(BASE_URL+"/"+account.getId())
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
        createAdminAccount();
        String token = getToken();

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

}
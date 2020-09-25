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

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
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
    }

    @Test
    public void testGet() throws Exception {
        // given
        Account adminAccount
                = createAdminAccount();
        String token = getToken();

        List<Account> allAccount = accountRepository.findAll();

        for (Account account: allAccount) {
            this.mockMvc.perform(get(BASE_URL + "/" + account.getId())
                    .header(HttpHeaders.AUTHORIZATION,"Bearer " + token))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("get"));
        }

    }

    @Test
    public void testUpdate() throws Exception {
        Account userAccount = createAdminAccount();

        AccountDto.UpdateRequest request = new AccountDto.UpdateRequest();
        request.setPassword("123");

        this.mockMvc.perform(patch(BASE_URL+"/"+userAccount.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("update"));
    }

    @Test
    public void testDelete() throws Exception {
        Account userAccount = createAdminAccount();

        this.mockMvc.perform(delete(BASE_URL+"/"+userAccount.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete"));

    }

}
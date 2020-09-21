package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.common.BaseTest;
import kr.ibct.springboilerplate.jwt.JwtTokenProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Todo REST_DOCS 적용하기
@ExtendWith(RestDocumentationExtension.class)
class AccountControllerTest extends BaseTest {

    String BASE_URL = "/api/v1/users";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    public Account generateUserAccount() {
        Account account = Account.builder()
                .email(userEmail)
                .password(userPassword)
                .roles(Set.of(AccountRole.USER))
                .build();
        accountService.save(account);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail,userPassword)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return account;
    }
    public Account generateAdminAccount() {
        Account account = Account.builder()
                .email(adminEmail)
                .password(adminPassword)
                .roles(Set.of(AccountRole.USER,AccountRole.ADMIN))
                .build();
        accountRepository.save(account);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminEmail,adminPassword)
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return account;
    }


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        accountRepository.deleteAll();

        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(documentationConfiguration(restDocumentation))
                .build();
    }

    @Test
    public void testSignUp() throws Exception {
        // given
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
    public void testGet() throws Exception {
        // given
        Account userAccount = generateUserAccount();

        this.mockMvc.perform(get(BASE_URL + "/" + userAccount.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andDo(document("get"));
    }

    @Test
    public void testUpdate() throws Exception {
        Account userAccount = generateUserAccount();

        AccountDto.updateRequest request = new AccountDto.updateRequest();
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
        Account userAccount = generateUserAccount();

        this.mockMvc.perform(delete(BASE_URL+"/"+userAccount.getId()))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andDo(document("delete"));

    }

}
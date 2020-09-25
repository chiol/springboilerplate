package kr.ibct.springboilerplate.account;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import kr.ibct.springboilerplate.account.AccountDto.SignInRequest;
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

//Todo REST_DOCS 적용하기
@ExtendWith(RestDocumentationExtension.class)
class AccountControllerTestByUnauth extends BaseTest {

    String BASE_URL = "/api/v1/users";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;


    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext,
                      RestDocumentationContextProvider restDocumentation) {
        accountRepository.deleteAll();

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
    }
//
//    @Test
//    public void testSignIn() throws Exception {
//        // given
//        createUserAccount();
//        SignInRequest request = new SignInRequest();
//        request.setEmail(userEmail);
//        request.setEmail(userPassword);
//
//        this.mockMvc.perform(post(BASE_URL+"/token")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("signIn"));
//    }
//
//    @Test
//    public void testGet() throws Exception {
//        // given
//        Account userAccount = createUserAccount();
//        String token = getToken();
//
//        this.mockMvc.perform(get(BASE_URL + "/" + userAccount.getId())
//                            .header(HttpHeaders.AUTHORIZATION,"Bearer " + token))
//                    .andDo(print())
//                    .andExpect(status().isOk())
//                    .andDo(document("get"));
//    }
//
//    @Test
//    public void testUpdate() throws Exception {
//        Account userAccount = createUserAccount();
//        String token = getToken();
//
//        AccountDto.UpdateRequest request = new AccountDto.UpdateRequest();
//        request.setPassword("123");
//
//        this.mockMvc.perform(
//                patch(BASE_URL+"/"+userAccount.getId())
//                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + token)
//            .contentType(MediaType.APPLICATION_JSON)
//            .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("update"));
//
//        this.mockMvc.perform(
//                patch(BASE_URL + "/" + userAccount.getId() + 1)
//                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + token)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//    }
//
//    @Test
//    public void testDelete() throws Exception {
//        Account userAccount = createUserAccount();
//        String token = getToken();
//
//        this.mockMvc.perform(
//                delete(BASE_URL+"/"+userAccount.getId())
//                    .header(HttpHeaders.AUTHORIZATION,"Bearer " + token))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andDo(document("delete"));
//
//        userAccount = createUserAccount();
//        token = getToken();
//        this.mockMvc.perform(
//                delete(BASE_URL+"/"+userAccount.getId() + 1)
//                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + token))
//                .andDo(print())
//                .andExpect(status().isUnauthorized());
//
//    }

}
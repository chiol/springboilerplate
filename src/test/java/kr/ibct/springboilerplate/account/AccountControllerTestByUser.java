package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.account.AccountDto.EmailAndPassword;
import kr.ibct.springboilerplate.account.AccountDto.GrantType;
import kr.ibct.springboilerplate.account.AccountDto.SignInRequest;
import kr.ibct.springboilerplate.common.BaseTest;
import kr.ibct.springboilerplate.jwt.JwtDto.AccessTokenAndRefreshToken;
import kr.ibct.springboilerplate.jwt.JwtTokenResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.security.auth.login.AccountNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//Todo REST_DOCS 적용하기
@ExtendWith(RestDocumentationExtension.class)
class AccountControllerTestByUser extends BaseTest {

    String BASE_URL = "/api/v1/users";

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;


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
    public void testSignIn() throws Exception {
        // given
        createUserAccount();

        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.setEmail(userEmail);
        emailAndPassword.setPassword(userPassword);

        this.mockMvc.perform(post(BASE_URL + "/token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(emailAndPassword)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("tokenType").value("Bearer"))
                .andExpect(jsonPath("accessToken").isString())
                .andExpect(jsonPath("accessTokenExpiresInDay").isNumber())
                .andExpect(jsonPath("refreshToken").isString())
                .andExpect(jsonPath("refreshTokenExpiresInDay").isNumber())
                .andDo(document("user/token"));
    }

    @Test
    public void testRefreshToken() throws Exception {
        Account userAccount = createUserAccount();
        EmailAndPassword emailAndPassword = new EmailAndPassword();
        emailAndPassword.setEmail(userEmail);
        emailAndPassword.setPassword(userPassword);

        AccessTokenAndRefreshToken accessTokenAndRefreshToken = accountService
                .provideToken(emailAndPassword);

        SignInRequest request = new SignInRequest();
        request.setEmail(userEmail);
        request.setPassword(userPassword);
        request.setGrantType(GrantType.refreshToken);
        request.setRefreshToken(accessTokenAndRefreshToken.getRefreshToken());


        MvcResult mvcResult = this.mockMvc
                .perform(post(BASE_URL + "/token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/givenRefreshToken"))
                .andReturn();

        String contentAsString = mvcResult.getResponse().getContentAsString();
        JwtTokenResponse jwtTokenResponse = objectMapper
                .readValue(contentAsString, JwtTokenResponse.class);

        // 사용자 정보와 refresh토큰이 맞지 않는 경우
        request.setEmail("user@Email.com");
        this.mockMvc
                .perform(post(BASE_URL + "/token").contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized())
                .andReturn();


        this.mockMvc.perform(get(BASE_URL + "/" + userAccount.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwtTokenResponse.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    public void testGetSuccess() throws Exception {
        // given
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        this.mockMvc.perform(get(BASE_URL + "/" + userAccount.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/get"));

    }

    @Test
    public void testGetErrorOtherUser() throws Exception {
        // given
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        this.mockMvc.perform(get(BASE_URL + "/" + userAccount.getId() + 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testPutSuccess() throws Exception {
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        AccountDto.PutRequest request = new AccountDto.PutRequest();
        request.setAddress("newAddress");
        request.setPhoneNum("newPhoneNum");
        request.setUsername("newUsername");

        // Success
        this.mockMvc.perform(
                put(BASE_URL + "/" + userAccount.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/put"));

        Account updateAccount = accountRepository.findByEmail(userEmail).orElseThrow(() -> new AccountNotFoundException("not Found"));
        assertThat(updateAccount.getAddress()).isEqualTo("newAddress");
        assertThat(updateAccount.getPhoneNum()).isEqualTo("newPhoneNum");
        assertThat(updateAccount.getUsername()).isEqualTo("newUsername");
    }

    @Test
    public void testPutErrorOtherUserInformation() throws Exception {
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        AccountDto.PutRequest request = new AccountDto.PutRequest();
        request.setAddress("newAddress");
        request.setPhoneNum("newPhoneNum");
        request.setUsername("newUsername");

        // 다른 아이디
        this.mockMvc.perform(
                put(BASE_URL + "/" + userAccount.getId() + 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // error
        request.setUsername(null);
        this.mockMvc.perform(
                put(BASE_URL + "/" + userAccount.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPutErrorRequestBodyNull() throws Exception {
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        AccountDto.PutRequest request = new AccountDto.PutRequest();
        request.setAddress(null);
        request.setPhoneNum("newPhoneNum");
        request.setUsername("newUsername");

        // error
        request.setUsername(null);
        this.mockMvc.perform(
                put(BASE_URL + "/" + userAccount.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPatchSuccess() throws Exception {
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setAddress("newAddress");


        this.mockMvc.perform(
                patch(BASE_URL + "/" + userAccount.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/patch"));

        Account account = accountRepository.findByEmail(userEmail).orElseThrow(() -> new AccountNotFoundException("not found"));
        assertThat(account.getAddress()).isEqualTo("newAddress");
        assertThat(account.getPhoneNum()).isEqualTo("phoneNum");


    }

    @Test
    public void testPatchErrorOtherUserRequest() throws Exception {
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setAddress("newAddress");

        this.mockMvc.perform(
                patch(BASE_URL + "/" + userAccount.getId() + 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }


    @Test
    public void testDelete() throws Exception {
        Account userAccount = createUserAccount();
        String token = getUserAccessToken();

        this.mockMvc.perform(
                delete(BASE_URL + "/" + userAccount.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isOk())
                .andDo(document("user/delete"));

        userAccount = createUserAccount();
        token = getUserAccessToken();
        this.mockMvc.perform(
                delete(BASE_URL + "/" + userAccount.getId() + 1)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andDo(print())
                .andExpect(status().isForbidden());

    }

}
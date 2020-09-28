package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.common.BaseTest;
import kr.ibct.springboilerplate.jwt.JwtDto;
import kr.ibct.springboilerplate.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

class AccountServiceTest extends BaseTest {

    String testEmail = "test@test.com";
    String testPassword = "testPassword";

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    public Account createUserAccount() {
        Account account = Account.builder()
                .email(userEmail)
                .password(userPassword)
                .address("address")
                .phoneNum("phoneNum")
                .username("username")
                .roles(Set.of(AccountRole.USER))
                .build();
        return accountService.save(account);
    }

    @BeforeEach
    public void setUp(WebApplicationContext webApplicationContext) {
        accountRepository.deleteAll();

        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .apply(springSecurity()) // security 추가
                .build();
    }

    @Test
    @Order(1)
    public void testSave() {
        Account account = new Account();
        account.setEmail(testEmail);
        account.setPassword(testPassword);
        account.setRoles(Set.of(AccountRole.USER));

        Account newAccount = accountService.save(account);
        assertThat(testEmail).isEqualTo(newAccount.getEmail());
        assertThat(passwordEncoder.matches(testPassword,newAccount.getPassword())).isTrue();

    }

    @Test
    public void testSaveAndLoadUserByEmail() {


        UserDetails userDetails = accountService.loadUserByUsername(testEmail);

        assertThat(testEmail).isEqualTo(userDetails.getUsername());
        assertThat(passwordEncoder.matches(testPassword,userDetails.getPassword())).isTrue();

    }
    @Test
    public void testSaveAndLoadUserById() {
        Account account = accountRepository.findByEmail(testEmail).orElseThrow();

        UserDetails userDetails = accountService.loadUserById(account.getId());

        assertThat(testEmail).isEqualTo(userDetails.getUsername());
        assertThat(passwordEncoder.matches(testPassword,userDetails.getPassword())).isTrue();
    }
    @Test
    void deleteAccount() {
        Account userAccount = createUserAccount();
        accountService.deleteAccount(userAccount.getId());
    }

    @Test
    void getAccount() {
        Account account = createUserAccount();

        AccountDto.GetResponse account1 = accountService.getAccount(account.getId());
        assertThat(account1.getId()).isEqualTo(account.getId());
        assertThat(account1.getEmail()).isEqualTo(account.getEmail());
        assertThat(account1.getCreated()).isEqualTo(account.getCreated());
        assertThat(account1.getUpdated()).isEqualTo(account.getUpdated());

    }

    @Test
    void getAllAccount() {
        List<Account> allAccount1 = accountService.getAllAccount();
        List<Account> allAccount2 = accountRepository.findAll();
        assertThat(allAccount1).isEqualTo(allAccount2);
    }

    @Test
    void testProvideToken() {
        Account userAccount = createUserAccount();
        AccountDto.EmailAndPassword emailAndPassword = new AccountDto.EmailAndPassword();
        emailAndPassword.setEmail(userEmail);
        emailAndPassword.setPassword(userPassword);
        JwtDto.AccessTokenAndRefreshToken accessTokenAndRefreshToken = accountService.provideToken(emailAndPassword);
        String accessToken = accessTokenAndRefreshToken.getAccessToken();
        String refreshToken = accessTokenAndRefreshToken.getRefreshToken();

        assertThat(jwtTokenProvider.getUserIdFromJwt(accessToken)).isEqualTo(userAccount.getId());
        assertThat(jwtTokenProvider.getUserIdFromJwt(refreshToken)).isEqualTo(userAccount.getId());
    }

    @Test
    void testProvideTokenGivenRefreshToken() {
        // given
        Account userAccount = createUserAccount();
        AccountDto.EmailAndPassword emailAndPassword = new AccountDto.EmailAndPassword();
        emailAndPassword.setEmail(userEmail);
        emailAndPassword.setPassword(userPassword);
        JwtDto.AccessTokenAndRefreshToken accessTokenAndRefreshToken = accountService.provideToken(emailAndPassword);
        String accessToken = accessTokenAndRefreshToken.getAccessToken();
        String refreshToken = accessTokenAndRefreshToken.getRefreshToken();

        //when

        String newAccessToken = accountService.provideToken(emailAndPassword, refreshToken);

        assertThat(newAccessToken).isNotEqualTo(accessToken);
        assertThat(jwtTokenProvider.getUserIdFromJwt(newAccessToken)).isEqualTo(userAccount.getId());

    }

    @Test
    void patchAccount() {
        Account userAccount = createUserAccount();
        AccountDto.PatchRequest request = new AccountDto.PatchRequest();
        request.setAddress("newAddress");

        accountService.patchAccount(request,userAccount);

        assertThat(userAccount.getAddress()).isEqualTo("newAddress");
        assertThat(userAccount.getPhoneNum()).isEqualTo("phoneNum");
        assertThat(userAccount.getUsername()).isEqualTo("username");
    }

    @Test
    void putAccount() {
        Account userAccount = createUserAccount();
        AccountDto.PutRequest request = new AccountDto.PutRequest();
        request.setAddress("newAddress");
        request.setUsername("newUsername");
        request.setPhoneNum("newPhoneNum");

        accountService.putAccount(request,userAccount);

        assertThat(userAccount.getAddress()).isEqualTo("newAddress");
        assertThat(userAccount.getPhoneNum()).isEqualTo("newPhoneNum");
        assertThat(userAccount.getUsername()).isEqualTo("newUsername");

        request.setUsername(null);

        accountService.putAccount(request,userAccount);

        assertThat(userAccount.getUsername()).isEqualTo("");
        assertThat(userAccount.getAddress()).isEqualTo("newAddress");
        assertThat(userAccount.getPhoneNum()).isEqualTo("newPhoneNum");
    }
}
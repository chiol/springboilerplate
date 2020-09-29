package kr.ibct.springboilerplate.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountRepository;
import kr.ibct.springboilerplate.account.AccountRole;
import kr.ibct.springboilerplate.account.AccountService;
import kr.ibct.springboilerplate.jwt.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseTest {

    @Value("${myapp.adminEmail}")
    protected String adminEmail;
    @Value("${myapp.adminPassword}")
    protected String adminPassword;
    @Value("${myapp.userEmail}")
    protected String userEmail;
    @Value("${myapp.userPassword}")
    protected String userPassword;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    protected ObjectMapper objectMapper;


    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void setUp() {
        accountRepository.deleteAll();
    }

    public Account createAdminAccount() {
        Account account = Account.builder()
                .email(adminEmail)
                .password(adminPassword)
                .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
                .build();
        return accountService.save(account);
    }

    public void createAccounts() {
        int total = 10;
        for (int i = 0; i < total; i++) {
            Account account = Account.builder()
                    .email(userEmail + i)
                    .password(adminPassword + i)
                    .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
                    .build();
            accountService.save(account);
        }
    }


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

    public String getAdminAccessToken() {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminEmail, adminPassword)
        );
        return jwtTokenProvider.generateAccessToken(authentication);
    }

    public String getUserAccessToken() {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userEmail, userPassword)
        );
        return jwtTokenProvider.generateAccessToken(authentication);
    }


}

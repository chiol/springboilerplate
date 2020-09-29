package kr.ibct.springboilerplate.jwt;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountRepository;
import kr.ibct.springboilerplate.account.AccountRole;
import kr.ibct.springboilerplate.account.AccountService;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JwtTokenProviderTest extends BaseTest {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

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


    @Test
    public void testGenerateToken() {
        Account userAccount = createUserAccount();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userEmail, userPassword);
        Authentication authentication = authenticationManager.authenticate(token);
        String s = jwtTokenProvider.generateAccessToken(authentication);

        assertThat(jwtTokenProvider.validateToken(s)).isTrue();
        assertThat(jwtTokenProvider.getUserIdFromJwt(s)).isEqualTo(userAccount.getId());
        System.out.println(jwtTokenProvider.getUserIdFromJwt(s));
    }
}
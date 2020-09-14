package kr.ibct.springboilerplate.account;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class AccountServiceTests extends BaseTest {

    String testEmail = "test@test.com";
    String testPassword = "testPassword";

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
}

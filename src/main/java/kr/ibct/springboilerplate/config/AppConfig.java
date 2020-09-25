package kr.ibct.springboilerplate.config;

import java.util.Set;
import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountRole;
import kr.ibct.springboilerplate.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AppConfig {

    @Value("${myapp.adminEmail}")
    private String adminEmail;
    @Value("${myapp.adminPassword}")
    private String adminPassword;
    @Value("${myapp.userEmail}")
    private String userEmail;
    @Value("${myapp.userPassword}")
    private String userPassword;

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) throws Exception {
                Account admin = Account.builder()
                        .email(adminEmail)
                        .password(adminPassword)
                        .roles(Set.of(AccountRole.USER, AccountRole.ADMIN))
                        .build();
                accountService.save(admin);

                Account user = Account.builder()
                        .email(userEmail)
                        .password(userPassword)
                        .roles(Set.of(AccountRole.USER))
                        .build();
                accountService.save(user);
            }
        };
    }
}

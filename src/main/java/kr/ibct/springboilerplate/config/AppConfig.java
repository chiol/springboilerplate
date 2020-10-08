package kr.ibct.springboilerplate.config;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountRole;
import kr.ibct.springboilerplate.account.AccountService;
import kr.ibct.springboilerplate.common.AuditorAwareAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Set;

@Configuration
@EnableAspectJAutoProxy
@EnableJpaAuditing
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
    public AuditorAware<Account> auditorAwareAccount() {
        return new AuditorAwareAccount();
    }

    @Bean
    public ApplicationRunner applicationRunner() {
        return new ApplicationRunner() {
            @Autowired
            AccountService accountService;

            @Override
            public void run(ApplicationArguments args) {
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

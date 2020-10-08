package kr.ibct.springboilerplate.common;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountAdapter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareAccount implements AuditorAware<Account> {
    @Override
    public Optional<Account> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(AccountAdapter.class::cast)
                .map(AccountAdapter::getAccount);
    }
}

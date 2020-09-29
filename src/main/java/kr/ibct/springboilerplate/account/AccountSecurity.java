package kr.ibct.springboilerplate.account;

import org.springframework.security.core.Authentication;

public class AccountSecurity {
    public boolean check(Authentication authentication, Long userId) {
        if (authentication.getPrincipal().equals("anonymousUser")) {
            return false;
        }
        AccountAdapter principal = (AccountAdapter) authentication.getPrincipal();
        Account account = principal.getAccount();
        return account.getId().equals(userId);
    }
}

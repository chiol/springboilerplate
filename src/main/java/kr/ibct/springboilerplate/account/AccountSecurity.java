package kr.ibct.springboilerplate.account;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class AccountSecurity {
    public boolean check(Authentication authentication, Long userId) {
        AccountAdapter principal = (AccountAdapter) authentication.getPrincipal();
        Account account = principal.getAccount();
        if (account.getId() != userId) {
            return false;
        }
        return true;
    }
}

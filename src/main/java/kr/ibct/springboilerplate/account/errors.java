package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.commons.errors.NotFoundException;
import org.springframework.security.core.AuthenticationException;

public class errors {
    public static class AccountIdNotFoundException extends NotFoundException {
        public AccountIdNotFoundException(String accountId) {
            super("["+ accountId + "] is not found");
        }
    }
    public static class AccountExistException extends RuntimeException {
        public AccountExistException(String message) {
            super(message);
        }
    }
    public static class AccountUnAuthentication extends AuthenticationException {
        public AccountUnAuthentication(String msg) {
            super(msg);
        }
    }
}

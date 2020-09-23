package kr.ibct.springboilerplate.account.exceptions;

import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.AuthenticationException;

public class AccountAuthorizationException extends AuthorizationServiceException {
    public AccountAuthorizationException(String msg) {
        super(msg);
    }
}

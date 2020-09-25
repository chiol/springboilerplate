package kr.ibct.springboilerplate.account.exceptions;

import org.springframework.security.access.AuthorizationServiceException;

public class AccountAuthorizationException extends AuthorizationServiceException {

    public AccountAuthorizationException(String msg) {
        super(msg);
    }
}

package kr.ibct.springboilerplate.account.exceptions;

public class AccountExistException extends RuntimeException {

    public AccountExistException(String message) {
        super(message);
    }
}

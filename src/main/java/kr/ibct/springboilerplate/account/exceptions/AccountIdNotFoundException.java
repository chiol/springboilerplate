package kr.ibct.springboilerplate.account.exceptions;

public class AccountIdNotFoundException extends RuntimeException {
    public AccountIdNotFoundException(String accountId) {
        super("["+ accountId + "] is not found");
    }
}
package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.commons.errors.NotFoundException;

public class erros {
    public static class AccountIdNotFoundException extends NotFoundException {
        public AccountIdNotFoundException(String accountId) {
            super("["+ accountId + "] is not found");
        }
    }
}

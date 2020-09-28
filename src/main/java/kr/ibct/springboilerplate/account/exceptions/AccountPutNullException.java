package kr.ibct.springboilerplate.account.exceptions;

public class AccountPutNullException extends NullPointerException{
    public AccountPutNullException(String s) {
        super(s);
    }
}

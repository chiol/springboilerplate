package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.account.exceptions.AccountAuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AccountAspect {

    @Pointcut("@annotation(kr.ibct.springboilerplate.account.AccessCheck)")
    private void AccessCheck() {}


    // Controller 의 파라미터 중 Long 타입과 @CurrentUser 의 Account 타입을 확인하여 해당 리소스에 접근할수 있는지(인가)를 확인한다.
    @Before("AccessCheck()")
    public void doAccessCheck(JoinPoint joinPoint) {
        Long id = null;
        Account account = null;
        Object[] args = joinPoint.getArgs();
        for (Object arg: args) {
            if (arg instanceof Account) {
                account = (Account) arg;
            }
            if (arg instanceof Long) {
                id = (Long) arg;
            }
        }
        if (id == null) {
            throw new RuntimeException("no id");
        }
        if (account == null) {
            throw new RuntimeException("Account not found");
        }

        if (!account.getRoles().contains(AccountRole.ADMIN)) {
            if (id != account.getId()) {
                throw new AccountAuthorizationException("your account does not have authentication for " + id);
            }
        }
    }
}

package kr.ibct.springboilerplate.querydsl;


import com.querydsl.core.types.Predicate;
import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountRepository;
import kr.ibct.springboilerplate.account.QAccount;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

public class QueryDslTest extends BaseTest {

    @Autowired
    private AccountRepository accountRepository;

    @Test
    public void testQueryDsl() {
        Account userAccount = createUserAccount();

        QAccount account = QAccount.account;

        Predicate predicate = account.id.eq(userAccount.getId());
        Account account1 = accountRepository.findOne(predicate).orElseThrow();

        assertThat(userAccount.getId()).isEqualTo(account1.getId());
        assertThat(userAccount).isEqualTo(account1);

    }
}

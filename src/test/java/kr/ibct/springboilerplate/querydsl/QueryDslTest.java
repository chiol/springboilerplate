package kr.ibct.springboilerplate.querydsl;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.QAccount;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;

public class QueryDslTest extends BaseTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void testQueryDsl() {
        QAccount account = QAccount.account;
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        Account account1 = queryFactory.selectFrom(account)
                .where(account.id.eq(1L))
                .fetchOne();
        System.out.println(account1);
    }
}

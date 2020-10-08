package kr.ibct.springboilerplate.account;

import com.querydsl.core.types.Predicate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, QuerydslPredicateExecutor<Account> {

    Optional<Account> findByEmail(String email);
}

package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.commons.errors.NotFoundException;
import kr.ibct.springboilerplate.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    public Account save(Account account) {
        accountRepository.findByEmail(account.getEmail()).ifPresent(account1 -> {
            throw new errors.AccountExistException("already exist email " + account1.getEmail());
        });
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        if (account.getRoles() == null) {
            account.setRoles(Set.of(AccountRole.USER));
        }
        account.setCreated(LocalDateTime.now());
        account.setUpdated(LocalDateTime.now());
        return this.accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = this.accountRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));
        return new AccountAdapter(account);
    }

    public UserDetails loadUserById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new errors.AccountIdNotFoundException(id.toString()));
        return new AccountAdapter(account);
    }


    public void updateAccount(Long id, AccountDto.updateRequest request) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new errors.AccountIdNotFoundException(id.toString()));
        AccountMapper.INSTANCE.updateRequestToAccount(request, account);
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        account.setUpdated(LocalDateTime.now());
        accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new errors.AccountIdNotFoundException(id.toString()));
        accountRepository.delete(account);
    }

    public AccountDto.accountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new errors.AccountIdNotFoundException(id.toString()));
        return AccountMapper.INSTANCE.toAccountResponse(account);
    }

    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    public String provideToken(String email, String password) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );
        String token = jwtTokenProvider.generateToken(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return token;

    }
}

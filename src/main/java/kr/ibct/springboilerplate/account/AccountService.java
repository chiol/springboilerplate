package kr.ibct.springboilerplate.account;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.transaction.Transactional;
import kr.ibct.springboilerplate.account.AccountDto.EmailAndPassword;
import kr.ibct.springboilerplate.account.exceptions.AccountExistException;
import kr.ibct.springboilerplate.account.exceptions.AccountIdNotFoundException;
import kr.ibct.springboilerplate.jwt.JwtDto;
import kr.ibct.springboilerplate.jwt.JwtDto.AccessTokenAndRefreshToken;
import kr.ibct.springboilerplate.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new AccountExistException("already exist email " + account1.getEmail());
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
        Account account = this.accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        return new AccountAdapter(account);
    }

    public UserDetails loadUserById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountIdNotFoundException(id.toString()));
        return new AccountAdapter(account);
    }


    public void updateAccount(Long id, AccountDto.UpdateRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountIdNotFoundException(id.toString()));
        AccountMapper.INSTANCE.updateRequestToAccount(request, account);
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        account.setUpdated(LocalDateTime.now());
        accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountIdNotFoundException(id.toString()));
        accountRepository.delete(account);
    }

    public AccountDto.GetResponse getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountIdNotFoundException(id.toString()));
        return AccountMapper.INSTANCE.toAccountResponse(account);
    }

    public List<Account> getAllAccount() {
        return accountRepository.findAll();
    }

    public AccessTokenAndRefreshToken provideToken(EmailAndPassword emailAndPassword) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailAndPassword.getEmail(), emailAndPassword.getPassword())
        );
        String accessToken = jwtTokenProvider.generateAccessToken(authentication);
        String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

        return new AccessTokenAndRefreshToken(accessToken, refreshToken);

    }
    public String provideToken(EmailAndPassword emailAndPassword, String refreshToken) {

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            // Todo: make exception about valid refresh token
            throw new RuntimeException("refreshToken not valid");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(emailAndPassword.getEmail(), emailAndPassword.getPassword())
        );
        return jwtTokenProvider.generateAccessToken(authentication);
    }
}

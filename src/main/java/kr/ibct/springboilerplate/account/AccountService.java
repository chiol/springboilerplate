package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.commons.errors.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class AccountService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    public Account save(Account account) {
        account.setPassword(this.passwordEncoder.encode(account.getPassword()));
        account.setCreated(LocalDateTime.now());
        account.setUpdated(LocalDateTime.now());
        return this.accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = this.accountRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(email));
        return new AccountAdapter(account);
    }

    public UserDetails loadUserById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new erros.AccountIdNotFoundException(id.toString()));
        return new AccountAdapter(account);
    }

    public void updateAccount(Long id, AccountDto.updateRequest request){
        Account account = accountRepository.findById(id).orElseThrow(() -> new erros.AccountIdNotFoundException(id.toString()));
        AccountMapper.INSTANCE.updateRequestToAccount(request, account);
        account.setUpdated(LocalDateTime.now());
        save(account);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new erros.AccountIdNotFoundException(id.toString()));
        accountRepository.delete(account);
    }
    public AccountDto.accountResponse getAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new erros.AccountIdNotFoundException(id.toString()));
        return AccountMapper.INSTANCE.toAccountResponse(account);
    }

}

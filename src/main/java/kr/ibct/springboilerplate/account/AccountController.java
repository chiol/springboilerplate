package kr.ibct.springboilerplate.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/api/v1/users")
public class AccountController {

    @Autowired
    AccountService accountService;


    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountDto.signUpRequest signUpRequest, Errors errors) {
        if (errors.hasErrors()) {

            return ResponseEntity.badRequest().body(errors);
        }

        Account account = AccountMapper.INSTANCE.signInRequestToAccount(signUpRequest);
        accountService.save(account);

        UriComponentsBuilder base = ServletUriComponentsBuilder.fromCurrentContextPath();
        MvcUriComponentsBuilder builder = MvcUriComponentsBuilder.relativeTo(base);
        UriComponents uriComponents = builder.withMethodCall(on(AccountController.class).signUp(signUpRequest, errors)).buildAndExpand(signUpRequest);
        URI uri = uriComponents.encode().toUri();
        return ResponseEntity.created(uri).body(signUpRequest);
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody AccountDto.SignInRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        String token = accountService.provideToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(token);
    }

    @GetMapping
    public ResponseEntity<?> getAllAccount() {
        List<Account> allAccount = accountService.getAllAccount();
        return ResponseEntity.ok(allAccount);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id, @CurrentUser Account account) {
        if (!verifyAccount(id, account)) {
            return ResponseEntity.badRequest().body("bad Request");
        }
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
                                           @Valid @RequestBody AccountDto.updateRequest request,
                                           @CurrentUser Account account,
                                           BindingResult errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        if (!verifyAccount(id, account)) {
            return ResponseEntity.badRequest().body("bad Request");
        }
        accountService.updateAccount(id, request);
        return ResponseEntity.ok().body("");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, @CurrentUser Account account) {
        if (!verifyAccount(id, account)) {
            return ResponseEntity.badRequest().body("bad Request");
        }
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }


    // Todo AOP or interceptor로 변경하기
    private boolean verifyAccount(Long id, Account account) {
        if (account.getRoles().contains(AccountRole.ADMIN)) {
            return true;
        }
        if (account.getId() == id) {
            return true;
        }
        return false;
    }


}

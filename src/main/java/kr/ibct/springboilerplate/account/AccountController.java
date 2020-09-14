package kr.ibct.springboilerplate.account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequestMapping("/api/v1/users")
public class AccountController {

    @Autowired
    AccountService accountService;


    @PostMapping
    public ResponseEntity<?> signUp(@RequestBody @Valid AccountDto.signUpRequest signUpRequest, BindingResult errors) {
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
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccount(@PathVariable Long id) {
        AccountDto.accountResponse account = accountService.getAccount(id);
        return ResponseEntity.ok(account);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
                                           @Valid @RequestBody AccountDto.updateRequest request,
                                           BindingResult errors){
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        accountService.updateAccount(id,request);
        return ResponseEntity.ok().body("");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }


}

package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.commons.ApiResponse;
import kr.ibct.springboilerplate.jwt.JwtTokenResponse;
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
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountDto.SignUpRequest signUpRequest, BindingResult errors) {
        if (errors.hasErrors()) {

            return ResponseEntity.badRequest().body(errors);
        }

        Account account = AccountMapper.INSTANCE.signInRequestToAccount(signUpRequest);
        accountService.save(account);

        ApiResponse apiResponse = new ApiResponse(true, "create account : " + signUpRequest.getEmail());

        UriComponentsBuilder base = ServletUriComponentsBuilder.fromCurrentContextPath();
        MvcUriComponentsBuilder builder = MvcUriComponentsBuilder.relativeTo(base);
        UriComponents uriComponents = builder.withMethodCall(on(AccountController.class).signUp(signUpRequest, errors)).buildAndExpand(apiResponse);
        URI uri = uriComponents.encode().toUri();

        return ResponseEntity.created(uri).body(apiResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody AccountDto.SignInRequest request, BindingResult errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        String token = accountService.provideToken(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(new JwtTokenResponse(token));
    }

    @GetMapping
    @AccessCheck
    public ResponseEntity<?> getAllAccount() {
        List<Account> allAccount = accountService.getAllAccount();
        return ResponseEntity.ok(allAccount);
    }

    @GetMapping("/{id}")
    @AccessCheck
    public ResponseEntity<?> getAccount(@PathVariable Long id, @CurrentUser Account account) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }


    // Todo account의 정보를 추가하여 patch와 put 만들기
    @PatchMapping("/{id}")
    @AccessCheck
    public ResponseEntity<?> updateAccount(@PathVariable Long id,
                                           @Valid @RequestBody AccountDto.UpdateRequest request,
                                           @CurrentUser Account account,
                                           BindingResult errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        accountService.updateAccount(id, request);
        return ResponseEntity.ok().body(new ApiResponse(true, "id" + id + "의 정보를 업데이트 했습니다.."));
    }


    @DeleteMapping("/{id}")
    @AccessCheck
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, @CurrentUser Account account) {

        accountService.deleteAccount(id);
        ApiResponse apiResponse = new ApiResponse(true, "id " + id + " 의 계정을 삭제했습니다.");
        return ResponseEntity.ok(apiResponse);
    }


}

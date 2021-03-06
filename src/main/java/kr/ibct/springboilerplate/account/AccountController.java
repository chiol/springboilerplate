package kr.ibct.springboilerplate.account;

import kr.ibct.springboilerplate.account.AccountDto.EmailAndPassword;
import kr.ibct.springboilerplate.account.AccountDto.GrantType;
import kr.ibct.springboilerplate.common.ApiResponse;
import kr.ibct.springboilerplate.jwt.JwtDto.AccessTokenAndRefreshToken;
import kr.ibct.springboilerplate.jwt.JwtTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
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
    // Todo JwtResponse에 설정값 주입
    @Value("${jwt.accessToken.expiresInDay}")
    private int accessTokenExpiresInDay;

    @Value("${jwt.refreshToken.expiresInDay}")
    private int refreshTokenExpiresInDay;


    @PostMapping
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountDto.SignUpRequest signUpRequest,
                                    BindingResult errors) {

        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }

        Account account = AccountMapper.INSTANCE.signInRequestToAccount(signUpRequest);
        Account newAccount = accountService.save(account);


        ApiResponse apiResponse = new ApiResponse(true,
                "create account (" + newAccount.getEmail() + ")");

        UriComponentsBuilder base = ServletUriComponentsBuilder.fromCurrentContextPath();
        MvcUriComponentsBuilder builder = MvcUriComponentsBuilder.relativeTo(base);
        UriComponents uriComponents = builder
                .withMethodCall(on(AccountController.class).signUp(signUpRequest, errors))
                .buildAndExpand(apiResponse);
        URI uri = uriComponents.encode().toUri();

        return ResponseEntity.created(uri).body(apiResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<?> signIn(@RequestBody @Valid AccountDto.SignInRequest request,
                                    BindingResult errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        if (request.getGrantType() == GrantType.refreshToken) {
            if (request.getRefreshToken() == null) {
                // Todo: not load refresh token
                throw new RuntimeException("do not have refresh token");
            }
            String accessToken = accountService.provideToken(
                    new EmailAndPassword(request.getEmail(), request.getPassword()),
                    request.getRefreshToken());
            // Todo JwtResponse 에 설정값 주입
            return ResponseEntity.ok(new JwtTokenResponse(
                    accessToken,
                    accessTokenExpiresInDay,
                    request.getRefreshToken(),
                    refreshTokenExpiresInDay
            ));
        }

        AccessTokenAndRefreshToken tokens = accountService.provideToken(
                new EmailAndPassword(request.getEmail(), request.getPassword())
        );
        return ResponseEntity.ok(new JwtTokenResponse(
                tokens.getAccessToken(),
                accessTokenExpiresInDay,
                tokens.getRefreshToken(),
                refreshTokenExpiresInDay
        ));

    }

    @GetMapping
    public ResponseEntity<?> getAllAccount() {
        List<Account> allAccount = accountService.getAllAccount();
        return ResponseEntity.ok(allAccount);
    }

    @GetMapping("/{id}")
    @AccessCheck
    public ResponseEntity<?> getAccount(@PathVariable Long id, @CurrentUser Account account) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }


    @PatchMapping("/{id}")
    @PreAuthorize("(hasRole('USER') and #id == #account.id) or hasRole('ADMIN')")
    public ResponseEntity<?> patchAccount(@PathVariable Long id,
                                          @Valid @RequestBody AccountDto.PatchRequest patchRequest,
                                          @CurrentUser Account account,
                                          BindingResult errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        accountService.patchAccount(patchRequest, account);
        return ResponseEntity.ok().body(new ApiResponse(true, "id " + id + "의 정보를 업데이트 했습니다."));
    }

    @PutMapping("/{id}")
    @PreAuthorize("(hasRole('USER') and #id == #account.id) or hasRole('ADMIN')")
    public ResponseEntity<?> putAccount(@PathVariable Long id,
                                        @Valid @RequestBody AccountDto.PutRequest putRequest,
                                        @CurrentUser Account account,
                                        BindingResult errors) {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest().body(errors);
        }
        accountService.putAccount(putRequest, account);
        return ResponseEntity.ok().body(new ApiResponse(true, "id " + id + "의 정보를 업데이트 했습니다."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id, @CurrentUser Account account) {

        accountService.deleteAccount(id);
        ApiResponse apiResponse = new ApiResponse(true, "id " + id + " 의 계정을 삭제했습니다.");
        return ResponseEntity.ok(apiResponse);
    }


}

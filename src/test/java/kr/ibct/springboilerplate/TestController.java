package kr.ibct.springboilerplate;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping
    public ResponseEntity<?> test() {
        return ResponseEntity.ok("testOk");
    }
    @GetMapping("/auth")
    public ResponseEntity<?> testAuth(@CurrentUser Account account) {
        return ResponseEntity.ok("testOk");
    }

    @GetMapping("/auth/admin")
    public ResponseEntity<?> testAdmin(@CurrentUser Account account) {
        return ResponseEntity.ok("testOk");
    }

}


package kr.ibct.springboilerplate.account;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class AccountDto {

    @Data
    public static class signUpRequest {
        @NotBlank
        @Size(max = 254)
        @Email
        private String email;
        @NotBlank
        private String password;
    }
    @Data
    public static class updateRequest {
        private String password;
    }
    @Data
    public static class deleteRequest {
        private String email;
    }
    @Data
    public static class accountResponse {
        private Long id;
        private String email;
        private LocalDateTime created;
        private LocalDateTime updated;
    }
}

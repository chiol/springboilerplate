package kr.ibct.springboilerplate.account;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

public class AccountDto {

    @Data
    public static class SignUpRequest {
        @NotBlank
        @Size(max = 254)
        @Email
        private String email;
        @NotBlank
        private String password;
    }
    @Data
    public static class SignInRequest {
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    @Data
    public static class UpdateRequest {
        private String password;
    }

    @Data
    public static class DeleteRequest {
        private String email;
    }

    @Data
    public static class GetResponse {
        private Long id;
        private String email;
        private LocalDateTime created;
        private LocalDateTime updated;
    }

}

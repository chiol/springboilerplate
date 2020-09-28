package kr.ibct.springboilerplate.account;

import java.time.LocalDateTime;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

        private GrantType grantType;
        private String refreshToken;
    }

    public enum GrantType {
        refreshToken
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EmailAndPassword {
        private String email;
        private String password;
    }

    @Data
    public static class PatchRequest {
        private String address;
        private String phoneNum;
        private String username;

    }

    @Data
    public static class PutRequest {
        @NotNull
        private String address;
        @NotNull
        private String phoneNum;
        @NotNull
        private String username;
    }

    @Data
    public static class DeleteRequest {

        private String email;
    }

    @Data
    public static class GetResponse {

        private Long id;
        private String email;

        private String phoneNum;
        private String address;
        private String username;

        private LocalDateTime created;
        private LocalDateTime updated;
    }

}

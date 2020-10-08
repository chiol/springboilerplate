package kr.ibct.springboilerplate.book;

import kr.ibct.springboilerplate.account.Account;
import lombok.Data;

import javax.validation.constraints.NotNull;

public class BookDto {
    @Data
    public static class CreateRequest {
        @NotNull
        private String title;
    }

    @Data
    public static class CreateBook {
        private String title;
        private Account author;
    }
}

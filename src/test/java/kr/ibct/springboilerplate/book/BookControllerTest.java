package kr.ibct.springboilerplate.book;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.common.BaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BookControllerTest extends BaseTest {

    private final String baseURL = "/api/v1/books";

    @Autowired
    private BookRepository bookRepository;

    @Test
    public void testCreate() throws Exception {
        Account userAccount = createUserAccount();
        String userAccessToken = getUserAccessToken();

        BookDto.CreateRequest request = new BookDto.CreateRequest();
        request.setTitle("book_title");
        this.mockMvc.perform(post(baseURL)
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + userAccessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

        Book book = bookRepository.findByTitle("book_title").orElseThrow();
        assertThat(book.getAuthor()).isEqualTo(userAccount);

    }

}
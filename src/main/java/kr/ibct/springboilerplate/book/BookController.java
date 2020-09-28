package kr.ibct.springboilerplate.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<?> getAllBooks(Pageable pageable) {
        Page<Book> byAllBooks = bookService.findByAllBooks(pageable);

        return ResponseEntity.ok(byAllBooks);
    }
}

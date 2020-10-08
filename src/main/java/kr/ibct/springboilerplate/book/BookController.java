package kr.ibct.springboilerplate.book;

import kr.ibct.springboilerplate.account.Account;
import kr.ibct.springboilerplate.account.AccountController;
import kr.ibct.springboilerplate.account.CurrentUser;
import kr.ibct.springboilerplate.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

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

    @PostMapping
    public ResponseEntity<?> createBook(@Valid @RequestBody BookDto.CreateRequest request, @CurrentUser Account author,
                                        BindingResult error) {
        if (error.hasErrors()) {
            return ResponseEntity.badRequest().body(error);
        }
        BookDto.CreateBook createBook = new BookDto.CreateBook();
        createBook.setTitle(request.getTitle());
        createBook.setAuthor(author);
        Book newBook = bookService.createBook(createBook);

        ApiResponse apiResponse = new ApiResponse(true,
                "create book (" + newBook.getTitle() + ")");

        UriComponentsBuilder base = ServletUriComponentsBuilder.fromCurrentContextPath();
        MvcUriComponentsBuilder builder = MvcUriComponentsBuilder.relativeTo(base);
        UriComponents uriComponents = builder
                .withMethodCall(on(BookController.class).createBook(request, author, error))
                .buildAndExpand(apiResponse);
        URI uri = uriComponents.encode().toUri();

        return ResponseEntity.created(uri).body(apiResponse);
    }

}

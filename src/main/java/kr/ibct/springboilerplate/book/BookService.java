package kr.ibct.springboilerplate.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Page<Book> findByAllBooks(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Book createBook(BookDto.CreateBook createBook) {
        Book book = new Book();
        book.setTitle(createBook.getTitle());
        book.setAuthor(createBook.getAuthor());
        return bookRepository.save(book);
    }
}

package kr.ibct.springboilerplate.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class BookCommandLineRunner implements CommandLineRunner {

    @Autowired
    private BookRepository repository;
    @Override
    public void run(String... args) throws Exception {
        List<Book> books = new ArrayList<>();
        int total = 10;
        for (int i = 0; i < total; ++i) {
            Book book = new Book();
            int randomInt = new Random().nextInt(100);
            book.setTitle("hello" + randomInt);
            book.setCreated(LocalDateTime.now());
            book.setUpdated(LocalDateTime.now());
            books.add(book);
        }
        repository.saveAll(books);

    }
}

package quarkus.bookstore.domain.service;

import quarkus.bookstore.application.dto.BookDto;
import quarkus.bookstore.application.dto.BooksDto;

import java.time.LocalDate;
import java.util.Optional;

public interface BookService {

    BookDto findBook(String isbn);

    BooksDto findAllBooks();

    BookDto create(String title, String description, Float unitCost, Integer pages, LocalDate publicationDate, String imageUrl, String bookLanguage);

    BookDto update(String isbn, Optional<String> title, Optional<String> description, Optional<Float> unitCost, Optional<Integer> pages, Optional<LocalDate> publicationDate, Optional<String> imageUrl, Optional<String> bookLanguage);

    void delete(String isbn);

}

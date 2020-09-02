package quarkus.bookstore.domain.model.repository;


import quarkus.bookstore.domain.model.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookRepository {

    List<Book> findAllBooks();

    Optional<Book> findByIsbn(String isbn);

    Book create(Book book);

    void remove(Book book);

    long countAll();

}

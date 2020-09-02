package quarkus.bookstore.infrastructure.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import quarkus.bookstore.domain.model.entity.Book;
import quarkus.bookstore.domain.model.repository.BookRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;

import static quarkus.bookstore.application.Constants.DEFAULT_SORT_COLUMN;

@ApplicationScoped
public class BookRepositoryImpl implements PanacheRepository<Book>, BookRepository {

    @Override
    public List<Book> findAllBooks() {
        return findAll(Sort.descending(DEFAULT_SORT_COLUMN)).list();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return find("isbn", isbn).firstResultOptional();
    }

    @Override
    public Book create(Book book) {
        persistAndFlush(book);
        return book;
    }

    @Override
    public void remove(Book book) {
        delete(book);
    }

    @Override
    public long countAll() {
        return count();
    }
}

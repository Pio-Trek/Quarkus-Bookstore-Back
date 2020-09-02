package quarkus.bookstore.infrastructure.api.model.response;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import quarkus.bookstore.application.dto.BooksDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@RegisterForReflection
public class BooksResponse {

    private List<BookResponse> books;
    private long booksCount;

    public BooksResponse(BooksDto booksDto) {
        this.books = booksDto.getBooks().stream().map(BookResponse::new).collect(Collectors.toList());
        this.booksCount = booksDto.getBooksCount();
    }
}

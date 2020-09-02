package quarkus.bookstore.application.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class BooksDto {

    private List<BookDto> books;
    private long booksCount;

}

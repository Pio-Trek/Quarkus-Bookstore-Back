package quarkus.bookstore.application.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import quarkus.bookstore.domain.model.entity.BookLanguage;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@ToString
@RegisterForReflection
public class BookDto {

    private String isbn;
    private String title;
    private String description;
    private Float unitCost;
    private Integer pages;
    private LocalDate publicationDate;
    private String imageUrl;
    private BookLanguage bookLanguage;

}

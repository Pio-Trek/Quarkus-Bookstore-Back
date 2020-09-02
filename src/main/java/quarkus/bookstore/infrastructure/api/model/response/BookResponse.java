package quarkus.bookstore.infrastructure.api.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.Getter;
import quarkus.bookstore.application.dto.BookDto;
import quarkus.bookstore.domain.model.entity.BookLanguage;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Getter
@JsonRootName("book")
@RegisterForReflection
public class BookResponse {

    private String isbn;
    private String title;
    private String description;
    private Float unitCost;
    private Integer pages;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate publicationDate;
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private BookLanguage bookLanguage;

    public BookResponse(BookDto bookDto) {
        this.isbn = bookDto.getIsbn();
        this.title = bookDto.getTitle();
        this.description = bookDto.getDescription();
        this.unitCost = bookDto.getUnitCost();
        this.pages = bookDto.getPages();
        this.publicationDate = bookDto.getPublicationDate();
        this.imageUrl = bookDto.getImageUrl();
        this.bookLanguage = bookDto.getBookLanguage();
    }
}

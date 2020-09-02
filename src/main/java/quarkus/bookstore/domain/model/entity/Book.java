package quarkus.bookstore.domain.model.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "BOOKS")
public class Book extends PanacheEntity {

    String isbn;

    String title;

    String description;

    @Column(name = "unit_cost")
    Float unitCost;

    Integer pages;

    @Column(name = "publication_date")
    LocalDate publicationDate;

    @Column(name = "image_url")
    String imageUrl;

    @Enumerated(EnumType.STRING)
    BookLanguage bookLanguage;

}

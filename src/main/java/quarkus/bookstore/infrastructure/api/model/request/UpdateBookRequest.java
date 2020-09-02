package quarkus.bookstore.infrastructure.api.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import quarkus.bookstore.domain.model.constants.ValidationMessages;
import quarkus.bookstore.infrastructure.api.validation.constraint.IsBookLanguageSupported;
import quarkus.bookstore.infrastructure.api.validation.constraint.OptionalNotBlank;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.Optional;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Valid
@JsonRootName("book")
@RegisterForReflection
public class UpdateBookRequest {

    private String title;

    private String description;

    private Float unitCost;

    private Integer pages;

    private LocalDate publicationDate;

    private String imageUrl;

    private String bookLanguage;

    public Optional<@OptionalNotBlank(message = ValidationMessages.TITLE_MUST_BE_NOT_BLANK) String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<@OptionalNotBlank(message = ValidationMessages.DESCRIPTION_MUST_BE_NOT_BLANK) String> getDescription() {
        return Optional.ofNullable(description);
    }

    public Optional<@Min(value = 1, message = ValidationMessages.UNIT_COST_MUST_BE_MORE_1) Float> getUnitCost() {
        return Optional.ofNullable(unitCost);
    }

    public Optional<@Min(value = 1, message = ValidationMessages.PAGES_COST_MUST_BE_MORE_1) Integer> getPages() {
        return Optional.ofNullable(pages);
    }

    public Optional<@Past(message = ValidationMessages.DATE_MUST_BE_PAST) LocalDate> getPublicationDate() {
        return Optional.ofNullable(publicationDate);
    }

    public Optional<String> getImageUrl() {
        return Optional.ofNullable(imageUrl);
    }

    public Optional<@IsBookLanguageSupported String> getBookLanguage() {
        return Optional.ofNullable(bookLanguage);
    }
}

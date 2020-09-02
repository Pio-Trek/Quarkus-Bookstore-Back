package quarkus.bookstore.infrastructure.api.model.request;

import com.fasterxml.jackson.annotation.JsonRootName;
import io.quarkus.runtime.annotations.RegisterForReflection;
import lombok.*;
import quarkus.bookstore.domain.model.constants.ValidationMessages;
import quarkus.bookstore.infrastructure.api.validation.constraint.IsBookLanguageSupported;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@JsonRootName("book")
@RegisterForReflection
public class NewBookRequest {

    @NotBlank(message = ValidationMessages.TITLE_MUST_BE_NOT_BLANK)
    private String title;

    @NotBlank(message = ValidationMessages.DESCRIPTION_MUST_BE_NOT_BLANK)
    private String description;

    @Min(value = 1, message = ValidationMessages.UNIT_COST_MUST_BE_MORE_1)
    private Float unitCost;

    @Min(value = 1, message = ValidationMessages.PAGES_COST_MUST_BE_MORE_1)
    private Integer pages;

    @Past(message = ValidationMessages.DATE_MUST_BE_PAST)
    @NotNull(message = ValidationMessages.DATE_MUST_BE_NOT_NULL)
    private LocalDate publicationDate;

    private String imageUrl;

    @IsBookLanguageSupported
    private String bookLanguage;

}

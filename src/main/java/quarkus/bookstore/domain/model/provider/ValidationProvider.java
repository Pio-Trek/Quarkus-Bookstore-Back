package quarkus.bookstore.domain.model.provider;

import quarkus.bookstore.domain.model.constants.ValidationMessages;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface ValidationProvider {

    <T> void validate(@Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL) T t);

}

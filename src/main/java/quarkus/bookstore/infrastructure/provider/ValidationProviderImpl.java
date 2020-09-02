package quarkus.bookstore.infrastructure.provider;

import quarkus.bookstore.domain.model.constants.ValidationMessages;
import quarkus.bookstore.domain.model.provider.ValidationProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@ApplicationScoped
public class ValidationProviderImpl implements ValidationProvider {

    @Override
    public <T> void validate(@Valid @NotNull(message = ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL) T t) {

    }
}

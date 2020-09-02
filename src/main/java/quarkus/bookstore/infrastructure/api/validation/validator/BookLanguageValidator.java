package quarkus.bookstore.infrastructure.api.validation.validator;

import org.apache.commons.lang3.StringUtils;
import quarkus.bookstore.domain.model.entity.BookLanguage;
import quarkus.bookstore.infrastructure.api.validation.constraint.IsBookLanguageSupported;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

public class BookLanguageValidator implements ConstraintValidator<IsBookLanguageSupported, String> {

    @Override
    public boolean isValid(String bookLanguage, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(bookLanguage) || StringUtils.isBlank(bookLanguage)) {
            return true;
        }

        return Arrays.stream(BookLanguage.values())
                .anyMatch(l -> l.name().equalsIgnoreCase(bookLanguage));
    }
}
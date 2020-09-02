package quarkus.bookstore.infrastructure.api.validation.constraint;

import quarkus.bookstore.infrastructure.api.validation.validator.BookLanguageValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR,
        PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {BookLanguageValidator.class})
public @interface IsBookLanguageSupported {

    String message() default "Unsupported book's language";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};


}

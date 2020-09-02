package quarkus.bookstore.domain.model.exception;

import lombok.Getter;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Getter
public class BusinessException extends RuntimeException {

    private String errorCode;
    private String errorMessage;

    public BusinessException(BookErrorCodes errorCodes) {
        super(errorCodes.toString());
        this.errorCode = errorCodes.name();
        this.errorMessage = errorCodes.getMessage();
    }

    public BusinessException(Throwable cause, BookErrorCodes errorCodes) {
        super(errorCodes.getMessage(), cause);
        this.errorCode = errorCodes.name();
        this.errorMessage = getExceptionMessage(cause);
    }

    private String getExceptionMessage(Throwable cause) {
        if (cause instanceof ConstraintViolationException) {
            List<String> messages = new ArrayList<>();
            ConstraintViolationException constraintViolationException = (ConstraintViolationException) cause;
            for (ConstraintViolation<?> cv : constraintViolationException.getConstraintViolations()) {
                messages.add(cv.getMessageTemplate());
            }
            return String.join("; ", messages);
        } else {
            return cause.getMessage();
        }
    }
}

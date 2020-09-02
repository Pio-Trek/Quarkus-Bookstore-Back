package quarkus.bookstore.domain.model.exception;

import lombok.Getter;

@Getter
public enum BookErrorCodes {

    BOOK_NOT_FOUND("Book not found"),
    BOOK_LANGUAGE_UNSUPPORTED("Unsupported book's language"),
    BOOK_CREATE_ERROR("Error during book creation request"),
    BOOK_UPDATE_ERROR("Error during update the book request");

    private String message;

    BookErrorCodes(String message) {
        this.message = message;
    }

}

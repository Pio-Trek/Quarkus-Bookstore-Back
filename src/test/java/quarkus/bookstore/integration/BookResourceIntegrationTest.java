package quarkus.bookstore.integration;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import quarkus.bookstore.domain.model.constants.ValidationMessages;
import quarkus.bookstore.domain.model.entity.Role;
import quarkus.bookstore.domain.model.entity.User;
import quarkus.bookstore.domain.model.exception.BookErrorCodes;
import quarkus.bookstore.infrastructure.api.model.request.NewBookRequest;
import quarkus.bookstore.infrastructure.api.model.request.UpdateBookRequest;

import javax.transaction.Transactional;
import javax.ws.rs.core.MediaType;
import java.time.LocalDate;
import java.util.Base64;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static quarkus.bookstore.constants.TestConstants.*;

@QuarkusTest
@Transactional
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
@DisplayName("Book Resource Integration Tests")
class BookResourceIntegrationTest {

    @BeforeAll
    void setUp() {
        User.add(TEST_USER_NAME, TEST_USER_PASSWORD, Role.TECHNICAL_USER);
    }

// --- GET BOOK ENDPOINT TESTS START ---

    @Test
    @Order(1)
    @DisplayName("Given valid ISBN when executing get book endpoint should return a single book")
    void give_valid_isbn_when_call_get_book_should_return_single_book() {

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .pathParam("isbn", "13-ISBN-TEST-1")
                .get(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(
                        "isbn",
                        is("13-ISBN-TEST-1"),
                        "title",
                        is("TEST_TITLE_1"),
                        "description",
                        is("TEST_DESCRIPTION_1"),
                        "unitCost",
                        is(99.99F),
                        "pages",
                        is(123),
                        "publicationDate",
                        is("2020-06-17"),
                        "imageUrl",
                        is("TEST_URL_1"),
                        "bookLanguage",
                        is("ENGLISH")
                );
    }

    @Test
    @Order(2)
    @DisplayName("Given invalid ISBN when executing get book endpoint should return book not found")
    void given_invalid_isbn_when_call_get_book_should_return_book_not_found() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .pathParam("isbn", "NO_EXIST")
                .get(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(
                        "$",
                        hasKey("timestamp"),
                        "status",
                        is("BAD_REQUEST"),
                        "code",
                        is(BookErrorCodes.BOOK_NOT_FOUND.name()),
                        "message",
                        is(BookErrorCodes.BOOK_NOT_FOUND.getMessage()),
                        "path",
                        is(BOOKS_API_PATH + "/NO_EXIST")
                );
    }

    // --- GET BOOK ENDPOINT TESTS END ---

    // --- GET BOOKS ENDPOINT TESTS START ---

    @Test
    @Order(3)
    @DisplayName("Should get a list of books with count number when executing get book")
    void should_get_list_books_and_count_number_when_call_get_books() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .get(BOOKS_API_PATH)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(
                        "booksCount",
                        is(2),
                        "books[0]",
                        hasKey("isbn"),
                        "books[0]",
                        hasKey("title"),
                        "books[0]",
                        hasKey("description"),
                        "books[0]",
                        hasKey("unitCost"),
                        "books[0]",
                        hasKey("pages"),
                        "books[0]",
                        hasKey("publicationDate"),
                        "books[0]",
                        hasKey("imageUrl"),
                        "books[0]",
                        hasKey("bookLanguage")
                );
    }

    // --- GET BOOKS ENDPOINT TESTS END ---

    // --- CREATE BOOK ENDPOINT TESTS START ---

    @Test
    @Order(4)
    @DisplayName("Given valid new book when executing create book endpoint should return created book")
    void given_valid_new_book_when_call_create_should_return_created_book() {
        NewBookRequest newBookRequest = createNewBook("TITLE", "DESCRIPTION", 99F, 1000, LocalDate.now().minusYears(2), "URL", "POLISH");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .body(newBookRequest)
                .post(BOOKS_API_PATH)
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body(
                        "isbn",
                        startsWith("13-978-"),
                        "title",
                        is(newBookRequest.getTitle()),
                        "description",
                        is(newBookRequest.getDescription()),
                        "unitCost",
                        is(newBookRequest.getUnitCost()),
                        "pages",
                        is(newBookRequest.getPages()),
                        "publicationDate",
                        is(String.valueOf(newBookRequest.getPublicationDate())),
                        "imageUrl",
                        is(newBookRequest.getImageUrl()),
                        "bookLanguage",
                        is(newBookRequest.getBookLanguage()));
    }

    @Test
    @Order(5)
    @DisplayName("Given missing new book object in request when executing create book endpoint should return book create error")
    void given_missing_new_book_object_in_body_when_call_create_should_return_book_create_error() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .post(BOOKS_API_PATH)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(
                        "$",
                        hasKey("timestamp"),
                        "status",
                        is("BAD_REQUEST"),
                        "code",
                        is(BookErrorCodes.BOOK_CREATE_ERROR.name()),
                        "message",
                        is(ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL),
                        "path",
                        is(BOOKS_API_PATH)
                );
    }

    @Order(6)
    @DisplayName("Given invalid new book when executing create book endpoint should return constraint violation error message")
    @ParameterizedTest(name = "Given book (title={0}, description={1}, unitCost={2}, pages={3}, publicationDate={4}, bookLanguage={5}) with wrong data should response \"{6}\"")
    @CsvSource({
            "'   ',     DESCRIPTION,     10.10,     100,     2020-01-01,     ENGLISH,     Title must not be blank",
            "TITLE,     '         ',     10.10,     100,     2020-01-01,     ENGLISH,     Description must not be blank",
            "TITLE,     DESCRIPTION,    -99.00,     100,     2020-01-01,     ENGLISH,     Unit cost must be more then 1",
            "TITLE,     DESCRIPTION,     10.10,       0,     2020-01-01,     ENGLISH,     Pages value must be more then 1",
            "TITLE,     DESCRIPTION,     10.10,     100,     2222-01-01,     ENGLISH,     Publication date must be in the past",
            "TITLE,     DESCRIPTION,     10.10,     100,               ,     ENGLISH,     Publication date must be not null",
            "TITLE,     DESCRIPTION,     10.10,     100,     2020-01-01,         ABC,     Unsupported book's language"
    })
    void given_invalid_new_book_when_call_create_should_return_constraint_violation_error(String title, String description, Float unitCost, Integer pages, LocalDate publicationDate, String bookLanguage, String errorMessage) {
        NewBookRequest newBookRequest = createNewBook(title, description, unitCost, pages, publicationDate, null, bookLanguage);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .body(newBookRequest)
                .post(BOOKS_API_PATH)
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(
                        "$",
                        hasKey("timestamp"),
                        "status",
                        is("BAD_REQUEST"),
                        "code",
                        is(BookErrorCodes.BOOK_CREATE_ERROR.name()),
                        "message",
                        is(errorMessage),
                        "path",
                        is(BOOKS_API_PATH));
    }

    // --- CREATE BOOK ENDPOINT TESTS END ---

    // --- UPDATE BOOK ENDPOINT TESTS START ---

    @Test
    @Order(7)
    @DisplayName("Given valid update book when executing update book endpoint should return updated book")
    void given_valid_update_book_when_call_update_should_return_updated_book() {
        UpdateBookRequest updateBookRequest = createUpdateBook("TITLE", "DESCRIPTION", 99F, 1000, LocalDate.now().minusYears(2), "URL", "POLISH");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .body(updateBookRequest)
                .pathParam("isbn", "13-ISBN-TEST-1")
                .put(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(
                        "isbn",
                        is("13-ISBN-TEST-1"),
                        "title",
                        is(updateBookRequest.getTitle().orElse(null)),
                        "description",
                        is(updateBookRequest.getDescription().orElse(null)),
                        "unitCost",
                        is(updateBookRequest.getUnitCost().orElse(null)),
                        "pages",
                        is(updateBookRequest.getPages().orElse(null)),
                        "publicationDate",
                        is(String.valueOf(updateBookRequest.getPublicationDate().orElse(null))),
                        "imageUrl",
                        is(updateBookRequest.getImageUrl().orElse(null)),
                        "bookLanguage",
                        is(updateBookRequest.getBookLanguage().orElse(null)));
    }

    @Test
    @Order(8)
    @DisplayName("Given missing update book object in request when executing update book endpoint should return book update error\"")
    void given_missing_update_book_object_in_body_when_call_update_book_should_return_book_update_error() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .pathParam("isbn", "13-ISBN-TEST-1")
                .put(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(
                        "$",
                        hasKey("timestamp"),
                        "status",
                        is("BAD_REQUEST"),
                        "code",
                        is(BookErrorCodes.BOOK_UPDATE_ERROR.name()),
                        "message",
                        is(ValidationMessages.REQUEST_BODY_MUST_BE_NOT_NULL),
                        "path",
                        is(BOOKS_API_PATH + "/13-ISBN-TEST-1")
                );
    }

    @Test
    @Order(9)
    @DisplayName("Given invalid ISBN when executing update book endpoint should return book not found")
    void given_invalid_isbn_when_call_update_should_return_book_not_found() {
        UpdateBookRequest updateBookRequest = createUpdateBook("TITLE", "DESCRIPTION", 99F, 1000, LocalDate.now().minusYears(2), "URL", "POLISH");

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .body(updateBookRequest)
                .pathParam("isbn", "NO_EXIST")
                .put(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(
                        "$",
                        hasKey("timestamp"),
                        "status",
                        is("BAD_REQUEST"),
                        "code",
                        is("BOOK_NOT_FOUND"),
                        "message",
                        is("Book not found"),
                        "path",
                        is(BOOKS_API_PATH + "/NO_EXIST")
                );
    }

    @Order(10)
    @DisplayName("Given invalid update book when executing update book endpoint should return constraint violation error message")
    @ParameterizedTest(name = "Given book (title={0}, description={1}, unitCost={2}, pages={3}, publicationDate={4}, bookLanguage={5}) with wrong data should response \"{6}\"")
    @CsvSource({
            "'   ',     DESCRIPTION,     10.10,     100,     2020-01-01,     ENGLISH,     Title must not be blank",
            "TITLE,     '         ',     10.10,     100,     2020-01-01,     ENGLISH,     Description must not be blank",
            "TITLE,     DESCRIPTION,    -99.00,     100,     2020-01-01,     ENGLISH,     Unit cost must be more then 1",
            "TITLE,     DESCRIPTION,     10.10,       0,     2020-01-01,     ENGLISH,     Pages value must be more then 1",
            "TITLE,     DESCRIPTION,     10.10,     100,     2222-01-01,     ENGLISH,     Publication date must be in the past",
            "TITLE,     DESCRIPTION,     10.10,     100,     2020-01-01,         ABC,     Unsupported book's language"
    })
    void given_invalid_update_book_when_call_update_should_return_constraint_violation_error(String title, String description, Float unitCost, Integer pages, LocalDate publicationDate, String bookLanguage, String errorMessage) {
        UpdateBookRequest updateBookRequest = createUpdateBook(title, description, unitCost, pages, publicationDate, null, bookLanguage);

        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .body(updateBookRequest)
                .pathParam("isbn", "13-ISBN-TEST-1")
                .put(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(
                        "$",
                        hasKey("timestamp"),
                        "status",
                        is("BAD_REQUEST"),
                        "code",
                        is(BookErrorCodes.BOOK_UPDATE_ERROR.name()),
                        "message",
                        is(errorMessage),
                        "path",
                        is(BOOKS_API_PATH + "/13-ISBN-TEST-1"));
    }

    // --- UPDATE BOOK ENDPOINT TESTS END ---

    // --- DELETE BOOK ENDPOINT TESTS START ---

    @Test
    @Order(11)
    @DisplayName("Given valid ISBN when executing delete book endpoint should return OK status")
    void given_valid_isbn_when_call_delete_should_return_ok() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .pathParam("isbn", "13-ISBN-TEST-2")
                .delete(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    @Order(12)
    @DisplayName("Given invalid ISBN when executing delete book endpoint should return book not found")
    void given_invalid_isbn_when_call_delete_should_return_book_not_found() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .header(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_VALUE_PREFIX + getPasswordAsBase64(TEST_USER_NAME, TEST_USER_PASSWORD))
                .pathParam("isbn", "NO_EXIST")
                .delete(BOOKS_API_PATH + "/{isbn}")
                .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body(
                        "$",
                        hasKey("timestamp"),
                        "status",
                        is("BAD_REQUEST"),
                        "code",
                        is("BOOK_NOT_FOUND"),
                        "message",
                        is("Book not found"),
                        "path",
                        is(BOOKS_API_PATH + "/NO_EXIST")
                );
    }

    // --- DELETE BOOK ENDPOINT TESTS END ---

    private NewBookRequest createNewBook(String title, String description, Float unitCost, Integer pages, LocalDate publicationDate, String imageUrl, String bookLanguage) {
        return NewBookRequest.builder()
                .title(title)
                .description(description)
                .unitCost(unitCost)
                .pages(pages)
                .publicationDate(publicationDate)
                .imageUrl(imageUrl)
                .bookLanguage(bookLanguage)
                .build();
    }

    private UpdateBookRequest createUpdateBook(String title, String description, Float unitCost, Integer pages, LocalDate publicationDate, String imageUrl, String bookLanguage) {
        return UpdateBookRequest.builder()
                .title(title)
                .description(description)
                .unitCost(unitCost)
                .pages(pages)
                .publicationDate(publicationDate)
                .imageUrl(imageUrl)
                .bookLanguage(bookLanguage)
                .build();
    }

    private String getPasswordAsBase64(String username, String password) {
        String credentials = username + ":" + password;
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

}
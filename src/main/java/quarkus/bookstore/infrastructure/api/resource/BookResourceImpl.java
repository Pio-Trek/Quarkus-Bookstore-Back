package quarkus.bookstore.infrastructure.api.resource;

import lombok.extern.jbosslog.JBossLog;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import quarkus.bookstore.application.dto.BookDto;
import quarkus.bookstore.application.dto.BooksDto;
import quarkus.bookstore.domain.model.exception.BookErrorCodes;
import quarkus.bookstore.domain.model.exception.BusinessException;
import quarkus.bookstore.domain.model.provider.ValidationProvider;
import quarkus.bookstore.domain.model.resource.BookResource;
import quarkus.bookstore.domain.service.BookService;
import quarkus.bookstore.infrastructure.api.model.request.NewBookRequest;
import quarkus.bookstore.infrastructure.api.model.request.UpdateBookRequest;
import quarkus.bookstore.infrastructure.api.model.response.BookResponse;
import quarkus.bookstore.infrastructure.api.model.response.BooksResponse;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

import static quarkus.bookstore.application.Constants.*;

@OpenAPIDefinition(
        info = @Info(
                title = "Bookstore-Back API",
                version = "1.0.0",
                contact = @Contact(
                        name = "Piotr Przechodzki",
                        url = "https://github.com/Pio-trek"),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"))
)
@ApplicationScoped
@Path("/books")
@Transactional
@JBossLog
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResourceImpl implements BookResource {

    @Context
    UriInfo uriInfo;

    private static final String CLAZZ = BookResourceImpl.class.getSimpleName();

    final BookService bookService;
    final ValidationProvider validationProvider;

    public BookResourceImpl(BookService bookService, ValidationProvider validationProvider) {
        this.bookService = bookService;
        this.validationProvider = validationProvider;
    }

    @Operation(summary = "Get book by its ISBN")
    @APIResponse(
            responseCode = "200",
            description = "Found the books",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class))}
    )
    @APIResponse(
            responseCode = "400",
            description = "Book not found",
            content = @Content
    )
    @GET
    @RolesAllowed("TECHNICAL_USER")
    @Path("/{isbn}")
    @Override
    public Response getBook(@PathParam("isbn") String isbn) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        String path = uriInfo.getAbsolutePath().getPath();
        log.info(String.format(LOG_FORMAT_API_PARAM, CLAZZ, method, path, isbn));
        BookDto bookDto = bookService.findBook(isbn);
        log.info(String.format(LOG_FORMAT_API_RESULT, CLAZZ, method, "book_isbn", bookDto.getIsbn()));
        return Response.ok(new BookResponse(bookDto)).build();
    }

    @Operation(summary = "Get a list of books with total count")
    @APIResponse(
            responseCode = "200",
            description = "Found list of books",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BooksResponse.class))}
    )
    @APIResponse(
            responseCode = "204",
            description = "No books",
            content = @Content
    )
    @GET
    @RolesAllowed("TECHNICAL_USER")
    @Override
    public Response getBooks() {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        String path = uriInfo.getAbsolutePath().getPath();
        log.info(String.format(LOG_FORMAT_API, CLAZZ, method, path));
        BooksDto booksDto = bookService.findAllBooks();
        log.info(String.format(LOG_FORMAT_API_RESULT, CLAZZ, method, "books_count", booksDto.getBooksCount()));
        return bookService.findAllBooks().getBooks().isEmpty() ? Response.noContent().build() : Response.ok(new BooksResponse(booksDto)).build();
    }

    @Operation(summary = "Create a new book")
    @APIResponse(
            responseCode = "201",
            description = "New book created",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class))}
    )
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content
    )
    @POST
    @RolesAllowed("TECHNICAL_USER")
    @Override
    public Response createBook(NewBookRequest newBookRequest) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        String path = uriInfo.getAbsolutePath().getPath();
        log.info(String.format(LOG_FORMAT_API_PARAM, CLAZZ, method, path, newBookRequest));
        validateBook(newBookRequest, BookErrorCodes.BOOK_CREATE_ERROR);
        BookDto bookDto = bookService.create(
                newBookRequest.getTitle(),
                newBookRequest.getDescription(),
                newBookRequest.getUnitCost(),
                newBookRequest.getPages(),
                newBookRequest.getPublicationDate(),
                newBookRequest.getImageUrl(),
                newBookRequest.getBookLanguage());

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(bookDto.getIsbn())
                .build();

        log.info(String.format(LOG_FORMAT_API_RESULT, CLAZZ, method, "book_created", bookDto));
        return Response.created(uri).entity(new BookResponse(bookDto)).build();
    }

    @Operation(summary = "Update a book")
    @APIResponse(
            responseCode = "200",
            description = "Book updated",
            content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = BookResponse.class))}
    )
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content
    )
    @PUT
    @Path("/{isbn}")
    @RolesAllowed("TECHNICAL_USER")
    @Override
    public Response updateBook(@PathParam("isbn") String isbn, UpdateBookRequest updateBookRequest) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        String path = uriInfo.getAbsolutePath().getPath();
        log.info(String.format(LOG_FORMAT_API_PARAM, CLAZZ, method, path, updateBookRequest));
        validateBook(updateBookRequest, BookErrorCodes.BOOK_UPDATE_ERROR);
        BookDto bookDto = bookService.update(
                isbn,
                updateBookRequest.getTitle(),
                updateBookRequest.getDescription(),
                updateBookRequest.getUnitCost(),
                updateBookRequest.getPages(),
                updateBookRequest.getPublicationDate(),
                updateBookRequest.getImageUrl(),
                updateBookRequest.getBookLanguage());

        log.info(String.format(LOG_FORMAT_API_RESULT, CLAZZ, method, "book_updated", bookDto));
        return Response.ok(new BookResponse(bookDto)).build();
    }

    @Operation(summary = "Delete a book by its ISBN")
    @APIResponse(
            responseCode = "200",
            description = "Book deleted",
            content = @Content
    )
    @APIResponse(
            responseCode = "400",
            description = "Bad request",
            content = @Content
    )
    @DELETE
    @Path("/{isbn}")
    @RolesAllowed("TECHNICAL_USER")
    @Override
    public Response deleteBook(@PathParam("isbn") String isbn) {
        String method = Thread.currentThread().getStackTrace()[1].getMethodName();
        String path = uriInfo.getAbsolutePath().getPath();
        log.info(String.format(LOG_FORMAT_API_PARAM, CLAZZ, method, path, isbn));
        bookService.delete(isbn);
        log.info(String.format(LOG_FORMAT_API_RESULT, CLAZZ, method, "book_deleted", isbn));
        return Response.ok().build();
    }

    private <T> void validateBook(T book, BookErrorCodes bookErrorCode) {
        try {
            validationProvider.validate(book);
        } catch (ConstraintViolationException e) {
            throw new BusinessException(e, bookErrorCode);
        }
    }
}
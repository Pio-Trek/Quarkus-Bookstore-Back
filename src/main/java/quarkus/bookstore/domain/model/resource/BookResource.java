package quarkus.bookstore.domain.model.resource;

import quarkus.bookstore.infrastructure.api.model.request.NewBookRequest;
import quarkus.bookstore.infrastructure.api.model.request.UpdateBookRequest;

import javax.ws.rs.core.Response;

public interface BookResource {

    Response getBook(String isbn);

    Response getBooks();

    Response createBook(NewBookRequest newBookRequest);

    Response updateBook(String isbn, UpdateBookRequest updateBookRequest);

    Response deleteBook(String isbn);

}

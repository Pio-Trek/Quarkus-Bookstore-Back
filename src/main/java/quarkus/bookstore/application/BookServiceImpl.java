package quarkus.bookstore.application;

import org.apache.commons.lang3.StringUtils;
import quarkus.bookstore.application.dto.BookDto;
import quarkus.bookstore.application.dto.BooksDto;
import quarkus.bookstore.domain.model.entity.Book;
import quarkus.bookstore.domain.model.entity.BookLanguage;
import quarkus.bookstore.domain.model.exception.BusinessException;
import quarkus.bookstore.domain.model.provider.NumberGeneratorProvider;
import quarkus.bookstore.domain.model.repository.BookRepository;
import quarkus.bookstore.domain.service.BookService;
import quarkus.bookstore.infrastructure.provider.TextSanitizeProvider;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static quarkus.bookstore.domain.model.exception.BookErrorCodes.BOOK_LANGUAGE_UNSUPPORTED;
import static quarkus.bookstore.domain.model.exception.BookErrorCodes.BOOK_NOT_FOUND;


@ApplicationScoped
public class BookServiceImpl implements BookService {

    final BookRepository bookRepository;
    final TextSanitizeProvider textProvider;
    final NumberGeneratorProvider generatorProvider;

    public BookServiceImpl(BookRepository bookRepository, TextSanitizeProvider textProvider, NumberGeneratorProvider generatorProvider) {
        this.bookRepository = bookRepository;
        this.textProvider = textProvider;
        this.generatorProvider = generatorProvider;
    }

    @Override
    public BooksDto findAllBooks() {
        List<Book> books = bookRepository.findAllBooks();
        long booksCount = bookRepository.countAll();
        return BooksDto.builder()
                .books(books.stream()
                        .map(this::convertToDto)
                        .collect(Collectors.toList()))
                .booksCount(booksCount)
                .build();
    }

    @Override
    public BookDto findBook(String isbn) {
        return convertToDto(bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException(BOOK_NOT_FOUND)));
    }

    @Override
    public BookDto create(String title, String description, Float unitCost, Integer pages, LocalDate publicationDate, String imageUrl, String bookLanguage) {
        Book book = bookRepository.create(createBook(title, description, unitCost, pages, publicationDate, imageUrl, bookLanguage));
        return convertToDto(book);
    }

    @Override
    public BookDto update(String isbn, Optional<String> title, Optional<String> description, Optional<Float> unitCost, Optional<Integer> pages, Optional<LocalDate> publicationDate, Optional<String> imageUrl, Optional<String> bookLanguage) {
        Book book = bookRepository.findByIsbn(isbn).orElseThrow(() -> new BusinessException(BOOK_NOT_FOUND));

        title.ifPresent(t -> book.setTitle(textProvider.sanitize(t)));
        description.ifPresent(book::setDescription);
        unitCost.ifPresent(book::setUnitCost);
        pages.ifPresent(book::setPages);
        publicationDate.ifPresent(book::setPublicationDate);
        imageUrl.ifPresent(book::setImageUrl);
        bookLanguage.ifPresent(bl -> book.setBookLanguage(convertToBookLanguage(bl)));

        return convertToDto(book);
    }

    @Override
    public void delete(String isbn) {
        Book book = bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new BusinessException(BOOK_NOT_FOUND));
        bookRepository.remove(book);
    }

    private Book createBook(String title, String description, Float unitCost, Integer pages, LocalDate publicationDate, String imageUrl, String bookLanguage) {
        Book book = new Book();
        book.setIsbn(generatorProvider.generateNumber());
        book.setTitle(textProvider.sanitize(title));
        book.setDescription(description);
        book.setUnitCost(unitCost);
        book.setPublicationDate(publicationDate);
        book.setImageUrl(imageUrl);
        book.setPages(pages);
        book.setBookLanguage(convertToBookLanguage(bookLanguage));
        return book;
    }

    private BookLanguage convertToBookLanguage(String bookLanguage) {
        if (StringUtils.isEmpty(bookLanguage) || StringUtils.isBlank(bookLanguage)) {
            return BookLanguage.NO_INFORMATION;
        }
        return Arrays.stream(BookLanguage.values())
                .filter(l -> l.name().equalsIgnoreCase(bookLanguage))
                .findFirst().orElseThrow(() -> new BusinessException(BOOK_LANGUAGE_UNSUPPORTED));
    }

    private BookDto convertToDto(Book book) {
        return BookDto.builder()
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .unitCost(book.getUnitCost())
                .pages(book.getPages())
                .publicationDate(book.getPublicationDate())
                .imageUrl(book.getImageUrl())
                .bookLanguage(book.getBookLanguage())
                .build();
    }

}

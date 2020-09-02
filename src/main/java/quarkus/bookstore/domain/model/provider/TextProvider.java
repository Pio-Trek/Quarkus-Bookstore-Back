package quarkus.bookstore.domain.model.provider;

public interface TextProvider {

    String sanitize(String textToSanitize);
}

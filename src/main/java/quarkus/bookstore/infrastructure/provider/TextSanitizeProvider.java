package quarkus.bookstore.infrastructure.provider;

import quarkus.bookstore.domain.model.provider.TextProvider;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TextSanitizeProvider implements TextProvider {

    public String sanitize(String textToSanitize) {
        return textToSanitize.replaceAll("\\s+", " ").trim();
    }
}

package quarkus.bookstore.infrastructure.provider;

import quarkus.bookstore.domain.model.provider.NumberGeneratorProvider;

import javax.enterprise.context.ApplicationScoped;
import java.util.Random;

@ApplicationScoped
public class IsbnGeneratorProviderProvider implements NumberGeneratorProvider {

    @Override
    public String generateNumber() {
        return "13-978-" + Math.abs(new Random().nextInt());
    }

}

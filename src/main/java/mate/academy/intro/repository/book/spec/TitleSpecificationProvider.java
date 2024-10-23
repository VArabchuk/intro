package mate.academy.intro.repository.book.spec;

import java.util.Arrays;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.SpecificationProvider;
import mate.academy.intro.repository.book.BookSearchCriteria;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return BookSearchCriteria.TITLE;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(BookSearchCriteria.TITLE).in(Arrays.stream(params)
                .toArray());
    }
}

package mate.academy.intro.repository.book;

import lombok.RequiredArgsConstructor;
import mate.academy.intro.dto.BookSearchParametersDto;
import mate.academy.intro.model.Book;
import mate.academy.intro.repository.SpecificationBuilder;
import mate.academy.intro.repository.SpecificationProviderManager;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (searchParametersDto.isbn() != null && searchParametersDto.isbn().length > 0) {
            specification
                    = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookSearchCriteria.ISBN)
                    .getSpecification(searchParametersDto.isbn()));
        }
        if (searchParametersDto.author() != null && searchParametersDto.author().length > 0) {
            specification
                    = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookSearchCriteria.AUTHOR)
                    .getSpecification(searchParametersDto.author()));
        }
        if (searchParametersDto.title() != null && searchParametersDto.title().length > 0) {
            specification
                    = specification.and(bookSpecificationProviderManager
                    .getSpecificationProvider(BookSearchCriteria.TITLE)
                    .getSpecification(searchParametersDto.title()));
        }
        return specification;
    }
}

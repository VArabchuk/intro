package mate.academy.intro.service;

import java.util.List;
import mate.academy.intro.dto.BookDto;
import mate.academy.intro.dto.BookSearchParametersDto;
import mate.academy.intro.dto.CreateBookRequestDto;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto createBookRequestDto);

    List<BookDto> getAll(Pageable pageable);

    BookDto getById(Long id);

    void deleteById(Long id);

    BookDto updateById(Long id, CreateBookRequestDto requestDto);

    List<BookDto> search(BookSearchParametersDto params, Pageable pageable);
}

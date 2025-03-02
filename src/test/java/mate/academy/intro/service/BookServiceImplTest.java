package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.BookSearchParametersDto;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.mapper.BookMapper;
import mate.academy.intro.model.Book;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.book.BookRepository;
import mate.academy.intro.repository.book.BookSpecificationBuilder;
import mate.academy.intro.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;
    @Mock
    private CategoryService categoryService;
    private final Book book = new Book();
    private final BookDto bookDto = new BookDto();

    @BeforeEach
    void beforeEach() {
        book.setId(1L).setTitle("Title").setAuthor("Author").setIsbn("ISBN");
        bookDto.setId(1L).setTitle("Title").setAuthor("Author").setIsbn("ISBN");
    }

    @Test
    @DisplayName("Save a valid book request and return BookResponseDto")
    public void save_ValidBookRequest_ReturnsBookResponseDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto().setIsbn("ISBN")
                .setCategoryIds(List.of(1L));

        when(categoryService.getCategoriesByIdIn(requestDto.getCategoryIds()))
                .thenReturn(Set.of(new Category()));
        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.save(requestDto);

        assertEquals(requestDto.getIsbn(), actual.getIsbn());
    }

    @Test
    @DisplayName("Returns all books from DB")
    public void getAll_ValidPageable_ReturnsAllBooks() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);
        List<BookDto> expected = List.of(bookDto);

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.getAll(pageable);

        assertEquals(expected.get(0).getIsbn(), actual.get(0).getIsbn());
    }

    @Test
    @DisplayName("Returns book with existing Id")
    public void getById_WithExistingId_ReturnsBookById() {
        Long bookId = book.getId();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        BookDto actual = bookService.getById(bookId);

        assertEquals(bookId, actual.getId());
    }

    @Test
    @DisplayName("Throws exception for non-existing book ID")
    public void getById_WithNotExistingId_ThrowsException() {
        Long notExistingId = 1L;

        when(bookRepository.findById(notExistingId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class,
                () -> bookService.getById(notExistingId));

        String expectedMessage = "Can't get a book by id: " + notExistingId;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Search books with valid parameters and pageable returns matching books")
    public void search_validParametersWithPageable_ReturnsBooksByParameters() {
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto(
                new String[]{"Title"},
                null,
                null
        );
        Specification<Book> bookSpecification = (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("title"), "Title");
        Page<Book> bookPage = new PageImpl<>(List.of(book), pageable, 1);

        when(bookSpecificationBuilder.build(searchParametersDto)).thenReturn(bookSpecification);
        when(bookRepository.findAll(bookSpecification, pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(bookDto);

        List<BookDto> actual = bookService.search(searchParametersDto, pageable);

        assertEquals(List.of(bookDto), actual);
    }
}

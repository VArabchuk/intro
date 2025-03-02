package mate.academy.intro.repository.book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mate.academy.intro.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest(properties = "spring.datasource.url=jdbc:tc:mysql:5.7:///book-store")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("Find books by category ID with pagination and verify expected books are returned")
    @Sql(scripts = {
            "classpath:database/add-categories-to-table.sql",
            "classpath:database/add-books-to-table.sql",
            "classpath:database/add-data-to-books_categories-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void findAllByCategoryId_ShouldReturnListOfBooksByCategory() {
        List<Book> actual = bookRepository.findAllByCategoryId(4L,
                PageRequest.of(0, 10));

        assertFalse(actual.isEmpty(),
                "Expected a non-empty list of books, but got an empty list");
        assertEquals(2, actual.size());
        assertEquals("Book One", actual.get(0).getTitle());
        assertEquals("Book Two", actual.get(1).getTitle());

        actual = bookRepository.findAllByCategoryId(3L,
                PageRequest.of(0, 10));

        assertFalse(actual.isEmpty(),
                "Expected a non-empty list of books, but got an empty list");
        assertEquals(1, actual.size());
        assertEquals("Book Three", actual.get(0).getTitle());

        Long nonExistingCategoryId = 20L;
        actual = bookRepository.findAllByCategoryId(nonExistingCategoryId,
                PageRequest.of(0, 10));

        assertTrue(actual.isEmpty(), "Expected empty list for non-existing category ID");
    }
}

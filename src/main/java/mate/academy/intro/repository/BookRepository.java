package mate.academy.intro.repository;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import mate.academy.intro.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BookRepository extends JpaRepository<Book, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Book b SET "
            + "b.title = :title"
            + ", b.author = :author"
            + ", b.isbn = :isbn"
            + ", b.price = :price"
            + ", b.description = :description"
            + ", b.coverImage = :coverImage "
            + "WHERE b.id = :id")
    void updateBookById(
            Long id,
            String title,
            String author,
            String isbn,
            BigDecimal price,
            String description,
            String coverImage
    );
}

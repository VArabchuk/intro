package mate.academy.intro.repository.category;

import java.util.List;
import java.util.Set;
import mate.academy.intro.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> getCategoriesByIdIn(List<Long> list);
}

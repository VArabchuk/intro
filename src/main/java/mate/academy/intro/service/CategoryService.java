package mate.academy.intro.service;

import java.util.List;
import java.util.Set;
import mate.academy.intro.dto.category.CategoryRequestDto;
import mate.academy.intro.dto.category.CategoryResponseDto;
import mate.academy.intro.model.Category;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    List<CategoryResponseDto> getAll(Pageable pageable);

    CategoryResponseDto getById(Long id);

    CategoryResponseDto save(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto update(Long id, CategoryRequestDto categoryRequestDto);

    void deleteById(Long id);

    Set<Category> getCategoriesByIdIn(List<Long> list);
}

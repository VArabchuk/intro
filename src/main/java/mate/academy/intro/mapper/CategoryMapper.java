package mate.academy.intro.mapper;

import mate.academy.intro.config.MapperConfig;
import mate.academy.intro.dto.category.CategoryRequestDto;
import mate.academy.intro.dto.category.CategoryResponseDto;
import mate.academy.intro.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface CategoryMapper {
    CategoryResponseDto toDto(Category category);

    Category toModel(CategoryRequestDto categoryRequestDto);

    void updateCategory(
            @MappingTarget Category category, CategoryRequestDto categoryRequestDto
    );
}

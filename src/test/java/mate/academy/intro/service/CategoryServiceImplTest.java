package mate.academy.intro.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import mate.academy.intro.dto.category.CategoryRequestDto;
import mate.academy.intro.dto.category.CategoryResponseDto;
import mate.academy.intro.exception.EntityNotFoundException;
import mate.academy.intro.mapper.CategoryMapper;
import mate.academy.intro.model.Category;
import mate.academy.intro.repository.category.CategoryRepository;
import mate.academy.intro.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("Update existing category by ID and return updated category")
    public void update_ExistingCategoryById_returnsUpdatedCategory() {
        Long id = 1L;
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("New name")
                .setDescription("New Description");
        Category existingCategory = new Category()
                .setId(id)
                .setName("Old Name")
                .setDescription("Old Description");
        Category updatedCategory = new Category()
                .setId(id).setName("New Name")
                .setDescription("New Description");
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(id)
                .setName("New Name")
                .setDescription("New Description");

        when(categoryRepository.findById(id)).thenReturn(Optional.of(existingCategory));
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.toDto(updatedCategory)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.update(id, categoryRequestDto);

        verify(categoryMapper).updateCategory(existingCategory, categoryRequestDto);
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual));
    }

    @Test
    @DisplayName("Updating a non-existing category should throw an exception")
    public void update_NonExistingCategoryId_ThrowsException() {
        Long nonExistingId = 1L;
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("New name")
                .setDescription("New Description");

        when(categoryRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(nonExistingId, categoryRequestDto));
        
        String expectedMessage = "Can't get a category by id: " + nonExistingId;
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Save category and return response DTO")
    public void save_ValidCategoryRequest_ReturnsCategoryResponseDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto()
                .setName("Name")
                .setDescription("Description");
        Category category = new Category()
                .setId(1L)
                .setName("Name")
                .setDescription("Description");
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(1L)
                .setName("Name")
                .setDescription("Description");

        when(categoryMapper.toModel(categoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toDto(category)).thenReturn(expected);

        CategoryResponseDto actual = categoryService.save(categoryRequestDto);

        EqualsBuilder.reflectionEquals(expected, actual);
    }
}

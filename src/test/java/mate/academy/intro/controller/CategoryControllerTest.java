package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import mate.academy.intro.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.intro.dto.category.CategoryRequestDto;
import mate.academy.intro.dto.category.CategoryResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @AfterEach
    void afterEach(@Autowired DataSource dataSource) throws SQLException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(connection,
                    new ClassPathResource("database/remove-all-data-from-tables.sql"));
        }
    }

    @Test
    @DisplayName("GET categories with valid Pageable returns all categories")
    @Sql(scripts = {"classpath:database/add-categories-to-table.sql"
    })
    @WithMockUser(username = "user", roles = "USER")
    public void getAll_ValidPageable_ReturnsAllCategories() throws Exception {
        List<CategoryResponseDto> expected = List.of(
                new CategoryResponseDto()
                        .setId(1L).setName("CATEGORY 1")
                        .setDescription("Description for CATEGORY 1"),
                new CategoryResponseDto()
                        .setId(2L).setName("CATEGORY 2")
                        .setDescription("Description for CATEGORY 2"),
                new CategoryResponseDto()
                        .setId(3L).setName("CATEGORY 3")
                        .setDescription("Description for CATEGORY 3"),
                new CategoryResponseDto()
                        .setId(4L).setName("CATEGORY 4")
                        .setDescription("Description for CATEGORY 4")
        );

        MvcResult result = mockMvc.perform(get("/categories")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<CategoryResponseDto> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(expected, actual, "The lists do not match");
    }

    @Test
    @DisplayName("GET /books by category ID with pageable returns book DTOs")
    @Sql(scripts = {"classpath:database/add-books-to-table.sql",
            "classpath:database/add-categories-to-table.sql",
            "classpath:database/add-data-to-books_categories-table.sql"
    })
    @WithMockUser(username = "user", roles = "USER")
    public void getBooksByCategoryId_ValidCategoryIdAndPageable_ReturnsBookDtos() throws Exception {
        Long categoryId = 4L;
        List<BookDtoWithoutCategoryIds> expected = List.of(
                new BookDtoWithoutCategoryIds().setId(1L).setTitle("Book One")
                        .setAuthor("Author One").setIsbn("111-1111111111")
                        .setPrice(BigDecimal.valueOf(10.99))
                        .setDescription("Description One").setCoverImage("cover1.jpg"),
                new BookDtoWithoutCategoryIds().setId(2L).setTitle("Book Two")
                        .setAuthor("Author Two").setIsbn("222-2222222222")
                        .setPrice(BigDecimal.valueOf(29.99))
                        .setDescription("Description Two").setCoverImage("cover2.jpg")
        );

        MvcResult mvcResult = mockMvc.perform(get("/categories/{id}/books", categoryId)
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<BookDtoWithoutCategoryIds> actual
                = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        assertEquals(expected, actual, "The lists do not match");
    }

    @Test
    @DisplayName("POST /categories creates a category and returns CategoryResponseDto")
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void createCategory_CategoryRequestDto_ReturnsCategoryResponseDto() throws Exception {
        CategoryRequestDto requestDto = new CategoryRequestDto().setName("CATEGORY 1")
                .setDescription("Description for CATEGORY 1");
        CategoryResponseDto expected = new CategoryResponseDto()
                .setId(1L).setName("CATEGORY 1")
                .setDescription("Description for CATEGORY 1");

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/categories")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        CategoryResponseDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), CategoryResponseDto.class);

        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"), "Objects are not equal. "
                + "Expected: " + expected + ", but got: " + actual);
    }
}

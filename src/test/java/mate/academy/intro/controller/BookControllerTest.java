package mate.academy.intro.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import mate.academy.intro.dto.book.BookDto;
import mate.academy.intro.dto.book.CreateBookRequestDto;
import mate.academy.intro.service.impl.BookServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
public class BookControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @SpyBean
    private BookServiceImpl bookService;

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
    @DisplayName("Successfully creates a new book")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/add-categories-to-table.sql")
    public void createBook_ValidRequestDto_Success() throws Exception {
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title").setAuthor("Author")
                .setPrice(BigDecimal.valueOf(1.20))
                .setIsbn("ISBN")
                .setDescription("Description")
                .setCoverImage("Cover Image")
                .setCategoryIds(List.of(1L));

        BookDto expected = new BookDto()
                .setId(1L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setPrice(requestDto.getPrice())
                .setIsbn(requestDto.getIsbn())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(post("/books")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertNotNull(actual.getId());
        assertTrue(EqualsBuilder.reflectionEquals(expected, actual, "id"),
                "Objects are not equal. Expected: " + expected + ", but got: " + actual);
    }

    @Test
    @DisplayName("Successfully updates an existing book")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = {"classpath:database/add-books-to-table.sql",
            "classpath:database/add-categories-to-table.sql",
    })
    public void updateBookById_ValidRequestDto_Success() throws Exception {
        Long idForUpdate = 2L;
        CreateBookRequestDto requestDto = new CreateBookRequestDto()
                .setTitle("Title").setAuthor("Author")
                .setPrice(BigDecimal.valueOf(1.20))
                .setIsbn("ISBN")
                .setDescription("Description")
                .setCoverImage("Cover Image")
                .setCategoryIds(List.of(1L));

        BookDto expected = new BookDto()
                .setId(2L)
                .setTitle(requestDto.getTitle())
                .setAuthor(requestDto.getAuthor())
                .setPrice(requestDto.getPrice())
                .setIsbn(requestDto.getIsbn())
                .setDescription(requestDto.getDescription())
                .setCoverImage(requestDto.getCoverImage())
                .setCategoryIds(List.of(1L));

        String jsonRequest = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(put("/books/{id}", idForUpdate)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        BookDto actual = objectMapper.readValue(result.getResponse()
                .getContentAsString(), BookDto.class);

        assertEquals(expected, actual,
                "Objects are not equal. Expected: " + expected + ", but got: " + actual);
    }

    @Test
    @DisplayName("DELETE /books/{id} - ADMIN gets 204 No Content")
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Sql(scripts = "classpath:database/add-books-to-table.sql")
    public void delete_WithExistingId_AsAdmin_ReturnsNoContent() throws Exception {
        Long existingId = 1L;

        mockMvc.perform(delete("/books/{id}", existingId))
                .andExpect(status().isNoContent());

        verify(bookService).deleteById(existingId);
    }

    @Test
    @DisplayName("DELETE /books/{id} - USER gets 403 Forbidden")
    @WithMockUser(username = "user", roles = "USER")
    public void delete_WithExistingId_AsUser_ReturnsForbidden() throws Exception {
        Long existingId = 1L;

        mockMvc.perform(delete("/books/{id}", existingId))
                .andExpect(status().isForbidden());
    }
}

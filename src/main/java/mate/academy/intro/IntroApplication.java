package mate.academy.intro;

import java.math.BigDecimal;
import mate.academy.intro.model.Book;
import mate.academy.intro.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class IntroApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(IntroApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("The Great Gatsby");
            book.setAuthor("Scott Fitzgerald");
            book.setCoverImage("https://commons.wikimedia.org/wiki/File:"
                    + "The_Great_Gatsby_Cover_1925_Retouched.jpg");
            book.setIsbn("978-0743273565");
            book.setPrice(BigDecimal.valueOf(2.49));
            book.setDescription("Novel by F. Scott Fitzgerald set in the 1920s");
            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}

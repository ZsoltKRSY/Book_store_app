import database.DatabaseConnectionFactory;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BookRepositoryMySQLTest {
    private static BookRepository bookRepository;

    @BeforeAll
    public static void setup(){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        bookRepository = new BookRepositoryMySQL(connection);
    }

    @BeforeEach
    public void clearBeforeEach(){
        bookRepository.removeAll();
    }

    @Test
    public void save1(){
        assertTrue(bookRepository.save(new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build()));
    }

    @Test
    public void save2(){
        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build();
        assertTrue(bookRepository.save(book));
    }

    @Test
    public void findAll1(){
        List<Book> books = bookRepository.findAll();
        assertEquals(0, books.size());
    }

    @Test
    public void findAll2(){
        Book book1 = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build();
        Book book2 = new BookBuilder()
                .setAuthor("Ioan Slavici")
                .setTitle("Moara cu noroc")
                .setPublishedDate(LocalDate.of(1950, 7, 3))
                .setPrice(500L)
                .setStock(4)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        List<Book> books = bookRepository.findAll();
        assertEquals(Arrays.asList(book1, book2), books);
    }

    @Test
    public void removeAll1(){
        bookRepository.removeAll();

        List<Book> books = bookRepository.findAll();
        assertTrue(books.isEmpty());
    }

    @Test
    public void removeAll2(){
        bookRepository.save(new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build());
        bookRepository.removeAll();

        List<Book> books = bookRepository.findAll();
        assertTrue(books.isEmpty());
    }

    @Test
    public void findById1(){
        final Optional<Book> book = bookRepository.findById(1L);
        assertTrue(book.isEmpty());
    }

    @Test
    public void findById2(){
        Book book1 = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build();
        Book book2 = new BookBuilder()
                .setAuthor("Ioan Slavici")
                .setTitle("Moara cu noroc")
                .setPublishedDate(LocalDate.of(1950, 7, 3))
                .setPrice(500L)
                .setStock(4)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        Book bookById = bookRepository.findAll().getFirst();
        Optional<Book> book = bookRepository.findById(bookById.getId());
        assertEquals(bookById, book.orElse(null));
    }

    @Test
    public void findByTitleAndAuthor(){
        Book book1 = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build();
        Book book2 = new BookBuilder()
                .setAuthor("Ioan Slavici")
                .setTitle("Moara cu noroc")
                .setPublishedDate(LocalDate.of(1950, 7, 3))
                .setPrice(500L)
                .setStock(4)
                .build();

        bookRepository.save(book1);
        bookRepository.save(book2);

        Optional<Book> book = bookRepository.findByTitleAuthorPublishedDate(book1.getTitle(), book1.getAuthor(), book1.getPublishedDate());
        assertEquals(book1, book.orElse(null));
    }

    @Test
    public void updateStock(){
        Book book1 = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build();

        bookRepository.save(book1);

        bookRepository.updateStock(book1, 5);
        Optional<Book> book = bookRepository.findByTitleAuthorPublishedDate(book1.getTitle(), book1.getAuthor(), book1.getPublishedDate());
        book.ifPresent(b -> assertEquals(5, b.getStock()));
    }
}

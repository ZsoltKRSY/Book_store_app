package repository.book;

import model.*;

import java.time.LocalDate;
import java.util.*;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate);

    boolean save(Book book);

    boolean delete(Book book);

    boolean updateStock(Book book, int newStock);

    void removeAll();
}

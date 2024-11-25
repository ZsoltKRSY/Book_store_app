package service.book;

import model.*;
import model.validator.Notification;

import java.time.LocalDate;
import java.util.*;

public interface BookService {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate);

    boolean save(Book book);

    boolean delete(Book book);

    boolean updateStock(Book book, int newStock);

    int getAgeOfBook(Long id);
}

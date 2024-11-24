package service.book;

import model.*;
import model.validator.Notification;

import java.util.*;

public interface BookService {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Optional<Book> findByTitleAndAuthor(String title, String author);

    boolean save(Book book);

    boolean delete(Book book);

    boolean updateStock(Book book, int newStock);

    int getAgeOfBook(Long id);
}

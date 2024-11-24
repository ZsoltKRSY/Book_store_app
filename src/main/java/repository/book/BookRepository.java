package repository.book;

import model.*;

import java.util.*;

public interface BookRepository {
    List<Book> findAll();

    Optional<Book> findById(Long id);

    Optional<Book> findByTitleAndAuthor(String title, String author);

    boolean save(Book book);

    boolean delete(Book book);

    boolean updateStock(Book book, int newStock);

    void removeAll();
}

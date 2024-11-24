package service.book;

import model.*;

import java.util.*;

public interface BookService {
    List<Book> findAll();

    Book findById(Long id) throws IllegalArgumentException;

    boolean save(Book book);

    boolean delete(Book book);

    boolean updateStock(Book book, int newStock);

    int getAgeOfBook(Long id);
}

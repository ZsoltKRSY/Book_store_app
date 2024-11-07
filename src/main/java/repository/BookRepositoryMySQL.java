package repository;

import model.Book;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";
        return null;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public boolean save(Book book) {
        return false;
    }

    @Override
    public boolean delete(Book book) {
        return false;
    }

    @Override
    public void removeAll() {

    }
}

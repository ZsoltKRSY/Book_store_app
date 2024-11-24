package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMySQL implements BookRepository {
    private final Connection connection;

    public BookRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public List<Book> findAll() {
        String sql = "SELECT * FROM book;";

        List<Book> books = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                books.add(getBookFromResultSet(resultSet));
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "SELECT * FROM book where id=?";

        Optional<Book> book = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            ResultSet resultSet =  preparedStatement.executeQuery();

            if (resultSet.next())
                book = Optional.of(getBookFromResultSet(resultSet));
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return book;
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, String author){
        String sql = "SELECT * FROM book where `title`=? and `author`=?";

        Optional<Book> book = Optional.empty();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, author);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next())
                book = Optional.of(getBookFromResultSet(resultSet));

        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return book;
    }

    @Override
    public boolean save(Book book) {
        String sql = "INSERT INTO book VALUES(null, ?, ?, ?, ?, ?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.setDate(3, java.sql.Date.valueOf(book.getPublishedDate()));
            preparedStatement.setLong(4, book.getPrice());
            preparedStatement.setInt(5, book.getStock());

            int rowsInserted = preparedStatement.executeUpdate();

            return rowsInserted == 1;

        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(Book book) {
        String sql = "DELETE FROM book WHERE author=? AND title=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, book.getAuthor());
            preparedStatement.setString(2, book.getTitle());
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public boolean updateStock(Book book, int newStock){
        String sql = "UPDATE book SET `stock`=? WHERE id=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, newStock);
            preparedStatement.setLong(2, book.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM book WHERE id >= 0;";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    private Book getBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new BookBuilder()
                .setId(resultSet.getLong("id"))
                .setTitle(resultSet.getString("title"))
                .setAuthor(resultSet.getString("author"))
                .setPublishedDate(new java.sql.Date(resultSet.getDate("publishedDate").getTime()).toLocalDate())
                .setPrice(resultSet.getLong("price"))
                .setStock(resultSet.getInt("stock"))
                .build();
    }
}

package repository.order;

import model.Book;
import model.Order;
import model.User;
import model.builder.BookBuilder;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderRepositoryMySQL implements OrderRepository {
    private final Connection connection;

    public OrderRepositoryMySQL(Connection connection){
        this.connection = connection;
    }

    @Override
    public boolean save(Order order) {
        String sql = "INSERT INTO `order` VALUES(null, ?, ?, ?);";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, order.empl_id());
            preparedStatement.setLong(2, order.book_id());
            preparedStatement.setTimestamp(3, java.sql.Timestamp.valueOf(order.orderDate()));

            int rowsInserted = preparedStatement.executeUpdate();

            return rowsInserted == 1;

        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    @Override
    public Map<Book, Integer> getBooksSoldByEmployee(User user) {
        Map<Book, Integer> booksSoldByEmployee = new HashMap<>();
        String sql = "SELECT b.id, b.title, b.author, b.publishedDate, b.price, b.stock " +
                "FROM `order` ord LEFT JOIN book b ON ord.book_id=b.id " +
                "WHERE b.id IS NOT NULL AND ord.empl_id=? AND ord.orderDate > NOW() - INTERVAL 30 DAY";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user.getId());

            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Book book = getBookFromResultSet(resultSet);
                booksSoldByEmployee.merge(book, 1, Integer::sum);
            }

            return booksSoldByEmployee;

        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return null;
    }

    @Override
    public void removeAll() {
        String sql = "DELETE FROM `order` WHERE id >= 0;";

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

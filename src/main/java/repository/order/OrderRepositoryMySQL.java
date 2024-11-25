package repository.order;

import model.Order;

import java.sql.*;

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
    public void removeAll() {
        String sql = "DELETE FROM `order` WHERE id >= 0;";

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);

        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }
}

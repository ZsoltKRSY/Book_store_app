package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBConnectionWrapper {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost/";
    private static final String USER = "root";
    private static final String PASSWORD = "Zsolika70!";
    private static final int TIMEOUT = 5;

    private Connection connection;

    public JDBConnectionWrapper(String schema){
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL + schema, USER, PASSWORD);

        } catch (ClassNotFoundException ex){
            ex.printStackTrace();
        } catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    public boolean testConnection() throws SQLException {
        return connection.isValid(TIMEOUT);
    }

    public Connection getConnection(){
        return connection;
    }
}

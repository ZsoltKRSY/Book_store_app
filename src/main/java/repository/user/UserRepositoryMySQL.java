package repository.user;
import model.User;
import model.builder.UserBuilder;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static database.Constants.Tables.USER;

public class UserRepositoryMySQL implements UserRepository {

    private final Connection connection;
    private final RightsRolesRepository rightsRolesRepository;


    public UserRepositoryMySQL(Connection connection, RightsRolesRepository rightsRolesRepository) {
        this.connection = connection;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public User findByUsernameAndPassword(String username, String password) {

        try {
            PreparedStatement findByUsernameAndPasswordPrepStatement = connection
                    .prepareStatement("SELECT * from ? WHERE `username`=? AND `password`=?");
            findByUsernameAndPasswordPrepStatement.setString(1, USER);
            findByUsernameAndPasswordPrepStatement.setString(2, username);
            findByUsernameAndPasswordPrepStatement.setString(3, password);

            ResultSet userResultSet = findByUsernameAndPasswordPrepStatement.executeQuery();

            if(userResultSet.next()) {
                return new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean save(User user) {
        try {
            PreparedStatement insertUserStatement = connection
                    .prepareStatement("INSERT INTO user values (null, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            insertUserStatement.setString(1, user.getUsername());
            insertUserStatement.setString(2, user.getPassword());
            insertUserStatement.executeUpdate();

            ResultSet rs = insertUserStatement.getGeneratedKeys();
            rs.next();
            long userId = rs.getLong(1);
            user.setId(userId);

            rightsRolesRepository.addRolesToUser(user, user.getRoles());

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void removeAll() {
        try {
            Statement statement = connection.createStatement();
            String sql = "DELETE from user where id >= 0";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existsByUsername(String username) {
        try {
            PreparedStatement existsByUsernamePrepStatement = connection
                    .prepareStatement("SELECT * FROM ? where `username`=?");
            existsByUsernamePrepStatement.setString(1, USER);
            existsByUsernamePrepStatement.setString(2, username);

            ResultSet userResultSet = existsByUsernamePrepStatement.executeQuery();

            return userResultSet.next();

        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

}
package repository.user;
import model.Book;
import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import repository.security.RightsRolesRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
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
        String sql = "SELECT * FROM user;";

        List<User> users = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()){
                User user = getUserFromResultSet(resultSet);
                user.setRoles(getRolesOfUser(user));

                users.add(user);
            }
        } catch (SQLException ex){
            ex.printStackTrace();
        }

        return users;
    }

    // SQL Injection Attacks should not work after fixing functions
    // Be careful that the last character in sql injection payload is an empty space
    // alexandru.ghiurutan95@gmail.com' and 1=1; --
    // ' or username LIKE '%admin%'; --

    @Override
    public Notification<User> findByUsernameAndPassword(String username, String password) {
        Notification<User> findByUsernameAndPasswordNotification = new Notification<>();

        try {
            PreparedStatement findByUsernameAndPasswordPrepStatement = connection
                    .prepareStatement("SELECT * FROM `" + USER + "` where `username`=? and `password`=?");
            findByUsernameAndPasswordPrepStatement.setString(1, username);
            findByUsernameAndPasswordPrepStatement.setString(2, password);

            ResultSet userResultSet = findByUsernameAndPasswordPrepStatement.executeQuery();

            if(userResultSet.next()) {
                User user = new UserBuilder()
                        .setUsername(userResultSet.getString("username"))
                        .setPassword(userResultSet.getString("password"))
                        .setId(userResultSet.getLong("id"))
                        .setRoles(rightsRolesRepository.findRolesForUser(userResultSet.getLong("id")))
                        .build();

                findByUsernameAndPasswordNotification.setResult(user);
            }
            else
                findByUsernameAndPasswordNotification.addError("Invalid username or password!");
        } catch (SQLException e) {
            //e.printStackTrace();
            findByUsernameAndPasswordNotification.addError("Something is wrong with the Database!");
        }

        return findByUsernameAndPasswordNotification;
    }

    @Override
    public Notification<Boolean> save(User user) {
        Notification<Boolean> saveUserNotification = new Notification<>();

        try {
            if(!existsByUsername(user.getUsername())){
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

                saveUserNotification.setResult(true);
            }
            else {
                saveUserNotification.addError("Username already taken!");
                saveUserNotification.setResult(false);
            }
        } catch (SQLException e) {
            //e.printStackTrace();
            saveUserNotification.addError("Something is wrong with the Database!");
            saveUserNotification.setResult(false);
        }

        return saveUserNotification;
    }

    @Override
    public boolean delete(User user){
        String sql = "DELETE FROM user WHERE id=? and username=?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, user.getId());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.executeUpdate();

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
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
                    .prepareStatement("SELECT * FROM `" + USER + "` where `username`=?");
            existsByUsernamePrepStatement.setString(1, username);

            ResultSet userResultSet = existsByUsernamePrepStatement.executeQuery();

            return userResultSet.next();

        } catch (SQLException ex){
            ex.printStackTrace();
            return false;
        }
    }

    private List<Role> getRolesOfUser(User user) throws SQLException {
        List<Role> roles = new ArrayList<>();

        String sql = "SELECT GROUP_CONCAT(r.role SEPARATOR ', ') AS roles " +
                "FROM user_role ur " +
                "LEFT JOIN role r ON ur.role_id=r.id " +
                "WHERE ur.user_id=?";

        PreparedStatement getRolesOfUserPrepStatement = connection.prepareStatement(sql);
        getRolesOfUserPrepStatement.setLong(1, user.getId());

        ResultSet resultSet = getRolesOfUserPrepStatement.executeQuery();
        String rolesString = "";
        if(resultSet.next())
            rolesString = resultSet.getString("roles");

        String[] rolesStringList = rolesString.split(", ");
        for (String role : rolesStringList)
            roles.add(new Role(null, role, null));

        return roles;
    }

    private User getUserFromResultSet(ResultSet resultSet) throws SQLException {
        return new UserBuilder()
                .setId(resultSet.getLong("id"))
                .setUsername(resultSet.getString("username"))
                .setPassword(resultSet.getString("password"))
                .build();
    }

}
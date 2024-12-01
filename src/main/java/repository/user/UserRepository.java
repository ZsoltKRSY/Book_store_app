package repository.user;

import model.User;
import model.validator.Notification;

import java.util.List;

public interface UserRepository {
    List<User> findAll();

    Notification<User> findByUsernameAndPassword(String username, String password);

    Notification<Boolean> save(User user);

    boolean delete(User user);

    void removeAll();

    boolean existsByUsername(String username);
}

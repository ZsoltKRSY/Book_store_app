package service.user;

import model.Role;
import model.User;
import model.validator.Notification;

import java.util.List;

public interface AdminUserService {

    Notification<Boolean> add(String username, String password, Role role);

    boolean delete(User user);

    List<User> findAll();

    List<Role> findAllRoles();
}

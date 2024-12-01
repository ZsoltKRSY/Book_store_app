package service.user;

import model.Role;
import model.User;
import model.builder.UserBuilder;
import model.validator.Notification;
import model.validator.UserValidator;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;

public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RightsRolesRepository rightsRolesRepository;

    public AdminUserServiceImpl(UserRepository userRepository, RightsRolesRepository rightsRolesRepository){
        this.userRepository = userRepository;
        this.rightsRolesRepository = rightsRolesRepository;
    }

    @Override
    public Notification<Boolean> add(String username, String password, Role role) {
        Role userRole = rightsRolesRepository.findRoleByTitle(role.getRole());

        User user = new UserBuilder()
                .setUsername(username)
                .setPassword(password)
                .setRoles(Collections.singletonList(userRole))
                .build();

        UserValidator userValidator = new UserValidator(user);
        boolean userValid = userValidator.validate();

        Notification<Boolean> addUserNotification = new Notification<>();
        if(!userValid) {
            userValidator.getErrors().forEach(addUserNotification::addError);
            addUserNotification.setResult(false);
        }
        else {
            user.setPassword(hashPassword(password));
            addUserNotification = userRepository.save(user);
        }

        return addUserNotification;
    }

    @Override
    public boolean delete(User user) {
        return userRepository.delete(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> findAllRoles() {
        return rightsRolesRepository.findAllRoles();
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

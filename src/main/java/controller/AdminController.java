package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import model.Role;
import model.User;
import model.validator.Notification;
import service.user.AdminUserService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

public class AdminController {
    private final AdminView adminView;
    private final AdminUserService adminUserService;
    private final User loggedUser;

    public AdminController(AdminView adminView, AdminUserService adminUserService, User loggedUser){
        this.adminView = adminView;
        this.adminUserService = adminUserService;
        this.loggedUser = loggedUser;

        this.adminView.addAddButtonListener(new AddButtonListener());
        this.adminView.addDeleteButtonListener(new DeleteButtonListener());
    }

    private class AddButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            String username = adminView.getUsername();
            String password = adminView.getPassword();
            Role role = adminView.getRole();

            if (username.isEmpty() || password.isEmpty())
                adminView.addDisplayAlertMessage("Save error", "Problem at input fields", "Can not have an empty Username or Password field!");
            else{
                Notification<Boolean> addUserNotification = adminUserService.add(username, password, role);

                if(addUserNotification.hasErrors())
                    adminView.addDisplayAlertMessage("Save error", "Invalid input data", addUserNotification.getFormattedErrors());
                else {
                    Notification<User> userNotification = adminUserService.findByUsernameAndPassword(username, password);

                    if(!userNotification.hasErrors()) {
                        UserDTO userDTO = new UserDTOBuilder()
                                .setId(userNotification.getResult().getId())
                                .setUsername(username)
                                .setRoles(FXCollections.observableArrayList(userNotification.getResult().getRoles()))
                                .build();
                        adminView.addUserToObservableList(userDTO);
                        adminView.addDisplayAlertMessage("Save successful", "User added", "User was successfully added!");
                    }
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            UserDTO userDTO = adminView.getUserTableView().getSelectionModel().getSelectedItem();
            if (userDTO != null){
                if(userDTO.getUsername().equals(loggedUser.getUsername()))
                    adminView.addDisplayAlertMessage("Delete error", "Problem at deleting user", "Can not delete the user you're logged in as!");
                else {
                    boolean deletionSuccessful = adminUserService.delete(UserMapper.convertUserDTOToUser(userDTO));

                    if (deletionSuccessful) {
                        adminView.removeUserFromObservableList(userDTO);
                        adminView.addDisplayAlertMessage("Delete successful", "User deleted", "User was successfully deleted!");
                    } else
                        adminView.addDisplayAlertMessage("Delete error", "Problem at deleting user", "There was a problem deleting the selected user!");
                }
            }
            else
                adminView.addDisplayAlertMessage("Delete error", "Problem at deleting user", "You must select a user before pressing the Delete button!");
        }
    }
}

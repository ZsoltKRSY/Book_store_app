package controller;
import database.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.*;
import model.Role;
import model.User;
import model.validator.Notification;
import service.user.AuthenticationService;
import view.LoginView;

import java.util.List;

public class LoginController {

    private final LoginView loginView;
    private final AuthenticationService authenticationService;


    public LoginController(LoginView loginView, AuthenticationService authenticationService) {
        this.loginView = loginView;
        this.authenticationService = authenticationService;

        this.loginView.addLoginButtonListener(new LoginButtonListener());
        this.loginView.addRegisterButtonListener(new RegisterButtonListener());
    }

    private class LoginButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(javafx.event.ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<User> loginNotification = authenticationService.login(username, password);

            if (loginNotification.hasErrors())
                loginView.setActionTargetText(loginNotification.getFormattedErrors());
            else{
                loginView.setActionTargetText("Login successful!");

                List<Role> userRole = loginNotification.getResult().getRoles();

                if (userRole.stream().anyMatch(role -> role.getRole().equals(Constants.Roles.ADMINISTRATOR))){
                    UserOperationsComponentsFactory instance = UserOperationsComponentsFactory.getInstance(UserOperationsComponentsFactory.getComponentsForTests());
                    AdminInterfaceComponentFactory.getInstance(UserOperationsComponentsFactory.getComponentsForTests(), LoginComponentFactory.getStage(), instance.getUserRepository(), instance.getRightsRolesRepository(), loginNotification.getResult());
                }
                else if (userRole.stream().anyMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE)))
                    BookStoreComponentFactory.getInstance(UserOperationsComponentsFactory.getComponentsForTests(), LoginComponentFactory.getStage(), loginNotification.getResult());
                else
                    CustomerScreenComponentFactory.getInstance(LoginComponentFactory.getStage());
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            Notification<Boolean> registerNotification = authenticationService.register(username, password);

            if (registerNotification.hasErrors())
                loginView.setActionTargetText(registerNotification.getFormattedErrors());
            else
                loginView.setActionTargetText("Register successful!");
        }
    }
}
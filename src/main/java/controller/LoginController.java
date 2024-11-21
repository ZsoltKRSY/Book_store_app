package controller;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import launcher.BookStoreComponentFactory;
import launcher.LoginComponentFactory;
import model.User;
import model.validator.UserValidator;
import service.user.AuthenticationService;
import view.LoginView;

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

            User loggedUser = authenticationService.login(username, password);

            if (loggedUser == null)
                loginView.setActionTargetText("Invalid username or password!");
            else{
                loginView.setActionTargetText("Login successful!");
                BookStoreComponentFactory.getInstance(LoginComponentFactory.getComponentsForTests(), LoginComponentFactory.getStage());
            }
        }
    }

    private class RegisterButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String username = loginView.getUsername();
            String password = loginView.getPassword();

            UserValidator userValidator = new UserValidator(username, password);

            if (userValidator.validate()){
                if(authenticationService.register(username, password))
                    loginView.setActionTargetText("Register successful!");
                else
                    loginView.setActionTargetText("Username already taken!");
            }
            else
                loginView.setActionTargetText(userValidator.getFormattedErrors());
        }
    }
}
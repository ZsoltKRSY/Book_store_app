package launcher;
import controller.LoginController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import model.Right;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;
import view.LoginView;

import java.sql.Connection;

public class LoginComponentFactory {

    private final LoginView loginView;
    private final LoginController loginController;
    private final AuthenticationService authenticationService;
    private static volatile LoginComponentFactory instance;
    private static Stage stage;

    public static LoginComponentFactory getInstance(Boolean aComponentsForTest, Stage aStage, UserRepository userRepository, RightsRolesRepository rightsRolesRepository) {
        if (instance == null) {
            synchronized (BookStoreComponentFactory.class) {
                if (instance == null) {
                    stage = aStage;
                    instance = new LoginComponentFactory(userRepository, rightsRolesRepository);
                }
            }
        }

        return instance;
    }

    private LoginComponentFactory(UserRepository userRepository, RightsRolesRepository rightsRolesRepository){
        this.authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);
        this.loginView = new LoginView(stage);
        this.loginController = new LoginController(loginView, authenticationService);
    }

    public static Stage getStage(){
        return stage;
    }


    public AuthenticationService getAuthenticationService(){
        return authenticationService;
    }

    public LoginView getLoginView(){
        return loginView;
    }

    public LoginController getLoginController(){
        return loginController;
    }

}
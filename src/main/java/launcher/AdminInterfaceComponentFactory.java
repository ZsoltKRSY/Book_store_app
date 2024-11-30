package launcher;

import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import model.User;
import view.AdminView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class AdminInterfaceComponentFactory {
    private final AdminView adminView;

    private static volatile AdminInterfaceComponentFactory instance;

    public static AdminInterfaceComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage, User loggedUser){
        if (instance == null){
            synchronized (AdminInterfaceComponentFactory.class) {
                if (instance == null)
                    instance = new AdminInterfaceComponentFactory(componentsForTest, primaryStage, loggedUser);
            }
        }

        return instance;
    }

    private AdminInterfaceComponentFactory(Boolean componentsForTest, Stage primaryStage, User loggedUser) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();

        List<UserDTO> userDTOs = null;
        this.adminView = new AdminView(primaryStage, userDTOs);
    }
}

package launcher;

import javafx.application.Application;
import javafx.stage.Stage;

public class Launcher extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        UserOperationsComponentsFactory instance = UserOperationsComponentsFactory.getInstance(false);
        LoginComponentFactory.getInstance(stage, instance.getUserRepository(), instance.getRightsRolesRepository());
    }
}

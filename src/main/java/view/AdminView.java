package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.UserDTO;

import java.util.List;

public class AdminView {

    private TableView<UserDTO> userTableView;
    private final ObservableList<UserDTO> usersObservableList;

    public AdminView(Stage primaryStage, List<UserDTO> UserDTOs){
        primaryStage.setTitle("Admin Interface");

        GridPane gridPane = new GridPane();
        //initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(UserDTOs);
        //initTableView(gridPane);

        //initSaveAndDelete(gridPane);

        primaryStage.show();
    }
}

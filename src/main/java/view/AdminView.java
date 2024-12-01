package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.Role;
import view.model.UserDTO;

import java.util.List;

public class AdminView {

    private TableView<UserDTO> userTableView;
    private final ObservableList<UserDTO> usersObservableList;
    private final ObservableList<Role> roles;

    private TextField usernameTextField;
    private PasswordField passwordField;
    private ComboBox<Role> roleComboBox;
    private Label usernameLabel;
    private Label passwordLabel;
    private Label roleLabel;
    private Button addButton;
    private Button deleteButton;
    private Button generateReport;

    public AdminView(Stage primaryStage, List<UserDTO> UserDTOs, List<Role> roles){
        primaryStage.setTitle("Admin Interface");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        usersObservableList = FXCollections.observableArrayList(UserDTOs);
        this.roles = FXCollections.observableArrayList(roles);
        initTableView(gridPane);

        initAddDeleteGenerate(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
        userTableView = new TableView<>();

        userTableView.setPlaceholder(new Label("No users to display."));

        TableColumn<UserDTO, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<UserDTO, String> usernameColumn = new TableColumn<>("Username");
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        TableColumn<UserDTO, List<Role>> rolesColumn = new TableColumn<>("Roles");
        rolesColumn.setCellValueFactory(new PropertyValueFactory<>("roles"));

        userTableView.getColumns().addAll(idColumn, usernameColumn, rolesColumn);

        userTableView.setItems(usersObservableList);

        gridPane.add(userTableView, 0, 0, 5, 1);
    }

    private void initAddDeleteGenerate(GridPane gridPane){
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 1, 1);
        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 2, 1);

        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 3, 1);
        passwordField = new PasswordField();
        gridPane.add(passwordField, 4, 1);

        roleLabel = new Label("Role");
        gridPane.add(roleLabel, 1, 2);
        roleComboBox = new ComboBox<>(roles);
        roleComboBox.getSelectionModel().selectLast();
        gridPane.add(roleComboBox, 2, 2);

        addButton = new Button("Add");
        gridPane.add(addButton, 5, 1);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 6, 1);

        generateReport = new Button("Generate Report");
        gridPane.add(generateReport, 6, 0);
    }
}

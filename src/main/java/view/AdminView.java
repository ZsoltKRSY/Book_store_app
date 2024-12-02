package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    private Button generateReportForUser;
    private Button generateReportForAllUsers;

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

        gridPane.add(userTableView, 0, 0, 5, 2);
    }

    private void initAddDeleteGenerate(GridPane gridPane){
        usernameLabel = new Label("Username");
        gridPane.add(usernameLabel, 1, 2);
        usernameTextField = new TextField();
        gridPane.add(usernameTextField, 2, 2);

        passwordLabel = new Label("Password");
        gridPane.add(passwordLabel, 3, 2);
        passwordField = new PasswordField();
        gridPane.add(passwordField, 4, 2);

        roleLabel = new Label("Role");
        gridPane.add(roleLabel, 1, 3);
        roleComboBox = new ComboBox<>(roles);
        roleComboBox.getSelectionModel().selectLast();
        gridPane.add(roleComboBox, 2, 3);

        addButton = new Button("Add");
        gridPane.add(addButton, 5, 2);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 6, 2);

        generateReportForUser = new Button("Generate Report for Employee");
        gridPane.add(generateReportForUser, 6, 0);

        generateReportForAllUsers = new Button("Generate Report for all Employees");
        gridPane.add(generateReportForAllUsers, 6, 1);
    }

    public void addAddButtonListener(EventHandler<ActionEvent> addButtonListener){
        addButton.setOnAction(addButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addGenerateReportForUserButtonListener(EventHandler<ActionEvent> generateReportButtonListener){
        generateReportForUser.setOnAction(generateReportButtonListener);
    }

    public void addGenerateReportForAllUsersButtonListener(EventHandler<ActionEvent> generateReportButtonListener){
        generateReportForAllUsers.setOnAction(generateReportButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public String getUsername(){
        return usernameTextField.getText();
    }

    public String getPassword(){
        return passwordField.getText();
    }

    public Role getRole(){
        return roleComboBox.getValue();
    }

    public void addUserToObservableList(UserDTO userDTO){
        this.usersObservableList.add(userDTO);
    }

    public void removeUserFromObservableList(UserDTO userDTO){
        this.usersObservableList.remove(userDTO);
    }

    public TableView<UserDTO> getUserTableView(){
        return userTableView;
    }
}

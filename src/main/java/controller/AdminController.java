package controller;

import database.Constants;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import mapper.UserMapper;
import model.Role;
import model.User;
import model.validator.Notification;
import service.report.ReportService;
import service.user.AdminUserService;
import view.AdminView;
import view.model.UserDTO;
import view.model.builder.UserDTOBuilder;

import java.util.Map;

public class AdminController {
    private final AdminView adminView;
    private final AdminUserService adminUserService;
    private final ReportService reportService;
    private final User loggedUser;

    public AdminController(AdminView adminView, AdminUserService adminUserService, ReportService reportService, User loggedUser){
        this.adminView = adminView;
        this.adminUserService = adminUserService;
        this.reportService = reportService;
        this.loggedUser = loggedUser;

        this.adminView.addAddButtonListener(new AddButtonListener());
        this.adminView.addDeleteButtonListener(new DeleteButtonListener());
        this.adminView.addGenerateReportForUserButtonListener(new GenerateReportForUserButtonListener());
        this.adminView.addGenerateReportForAllUsersButtonListener(new GenerateReportForAllUsersButtonListener());
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

    private class GenerateReportForUserButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            UserDTO userDTO = adminView.getUserTableView().getSelectionModel().getSelectedItem();
            if(userDTO != null) {
                if(userDTO.getRoles().stream().noneMatch(role -> role.getRole().equals(Constants.Roles.EMPLOYEE)))
                    adminView.addDisplayAlertMessage("Generate Report error", "Problem at generating Sales Report for Employee", "The selected user is not an Employee!");
                else{
                    if(reportService.generateEmployeeSalesReport(UserMapper.convertUserDTOToUser(userDTO), "D:\\Munkak\\An3\\Sem1\\IS\\Library\\EmployeeReport_" + userDTO.getUsername() + ".pdf"))
                        adminView.addDisplayAlertMessage("Generate Report success", "Report generated for Employee", "Successfully generated Sales Report for Employee " + userDTO.getUsername() + "!");
                    else
                        adminView.addDisplayAlertMessage("Generate Report error", "Problem at generating Report for Employee", "There was a problem generating the Sales Report for Employee " + userDTO.getUsername() + "!");
                }
            }
            else
                adminView.addDisplayAlertMessage("Generate Report error", "Problem at generating Report for Employee", "You must select an Employee before pressing the Generate Report for Employee button!");
        }
    }

    private class GenerateReportForAllUsersButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            if(reportService.generateAllEmployeesSalesReport("D:\\Munkak\\An3\\Sem1\\IS\\Library\\EmployeesReport.pdf"))
                adminView.addDisplayAlertMessage("Generate Report success", "Report generated for all Employees", "Successfully generated Sales Report for all Employees!");
            else
                adminView.addDisplayAlertMessage("Generate Report error", "Problem at generating Report for all Employee", "There was a problem generating the Sales Report for all Employees!");
        }
    }
}

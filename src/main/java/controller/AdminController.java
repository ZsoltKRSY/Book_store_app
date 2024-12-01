package controller;

import model.User;
import service.user.AdminUserService;
import view.AdminView;

public class AdminController {
    private final AdminView adminView;
    private final AdminUserService adminUserService;
    private final User loggedUser;

    public AdminController(AdminView adminView, AdminUserService adminUserService, User loggedUser){
        this.adminView = adminView;
        this.adminUserService = adminUserService;
        this.loggedUser = loggedUser;
    }
}

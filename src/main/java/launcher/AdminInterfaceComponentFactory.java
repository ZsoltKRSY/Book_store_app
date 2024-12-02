package launcher;

import controller.AdminController;
import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.UserMapper;
import model.User;
import repository.order.OrderRepository;
import repository.security.RightsRolesRepository;
import repository.user.UserRepository;
import service.report.ReportService;
import service.report.ReportServiceImpl;
import service.user.AdminUserService;
import service.user.AdminUserServiceImpl;
import view.AdminView;
import view.model.UserDTO;

import java.sql.Connection;
import java.util.List;

public class AdminInterfaceComponentFactory {
    private final AdminView adminView;
    private final AdminController adminController;
    private final AdminUserService adminUserService;
    private final ReportService reportService;
    private static volatile AdminInterfaceComponentFactory instance;

    public static AdminInterfaceComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage, UserRepository userRepository, RightsRolesRepository rightsRolesRepository, OrderRepository orderRepository, User loggedUser){
        if (instance == null){
            synchronized (AdminInterfaceComponentFactory.class) {
                if (instance == null)
                    instance = new AdminInterfaceComponentFactory(componentsForTest, primaryStage, userRepository, rightsRolesRepository, orderRepository, loggedUser);
            }
        }

        return instance;
    }

    private AdminInterfaceComponentFactory(Boolean componentsForTest, Stage primaryStage, UserRepository userRepository, RightsRolesRepository rightsRolesRepository, OrderRepository orderRepository, User loggedUser) {
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.adminUserService = new AdminUserServiceImpl(userRepository, rightsRolesRepository);
        this.reportService = new ReportServiceImpl(userRepository, orderRepository);

        List<UserDTO> userDTOs = UserMapper.convertUserListToUserDTOList(adminUserService.findAll());
        this.adminView = new AdminView(primaryStage, userDTOs, adminUserService.findAllRoles());
        this.adminController = new AdminController(adminView, adminUserService, reportService, loggedUser);
    }

    public AdminView getAdminView() {
        return adminView;
    }

    public AdminController getAdminController() {
        return adminController;
    }

    public AdminUserService getAdminUserService() {
        return adminUserService;
    }
}

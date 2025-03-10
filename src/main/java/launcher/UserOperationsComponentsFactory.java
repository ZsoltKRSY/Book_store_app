package launcher;

import database.DatabaseConnectionFactory;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;

public class UserOperationsComponentsFactory {
    private final RightsRolesRepository rightsRolesRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private static Boolean componentsForTests;
    private static volatile UserOperationsComponentsFactory instance;

    public static UserOperationsComponentsFactory getInstance(Boolean aComponentsForTest) {
        if (instance == null) {
            synchronized (BookStoreComponentFactory.class) {
                if (instance == null) {
                    componentsForTests = aComponentsForTest;
                    instance = new UserOperationsComponentsFactory(aComponentsForTest);
                }
            }
        }

        return instance;
    }

    public UserOperationsComponentsFactory(Boolean componentsForTests){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTests).getConnection();

        this.rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        this.userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
        this.orderRepository = new OrderRepositoryMySQL(connection);
    }

    public static Boolean getComponentsForTests(){
        return componentsForTests;
    }

    public UserRepository getUserRepository(){
        return userRepository;
    }

    public RightsRolesRepository getRightsRolesRepository(){
        return rightsRolesRepository;
    }

    public OrderRepository getOrderRepository(){
        return orderRepository;
    }
}

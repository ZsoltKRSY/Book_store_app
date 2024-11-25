import database.DatabaseConnectionFactory;
import model.Book;
import model.Role;
import model.User;
import model.Order;

import model.builder.BookBuilder;
import model.builder.UserBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.Collections;

import static database.Constants.Roles.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderRepositoryMySQLTest {
    private static OrderRepository orderRepository;
    private static BookRepository bookRepository;
    private static RightsRolesRepository rightsRolesRepository;
    private static UserRepository userRepository;

    @BeforeAll
    public static void setup(){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();
        orderRepository = new OrderRepositoryMySQL(connection);
        bookRepository = new BookRepositoryMySQL(connection);
        rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        rightsRolesRepository.addRole("random_role");
        userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);
    }

    @BeforeEach
    public void clearBeforeEach(){
        orderRepository.removeAll();
        bookRepository.removeAll();
        userRepository.removeAll();
    }

    @Test
    public void save(){
        Book book = new BookBuilder()
                .setTitle("Ion")
                .setAuthor("Liviu Rebreanu")
                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
                .setPrice(515L)
                .setStock(2)
                .build();
        bookRepository.save(book);

        Role userRole = rightsRolesRepository.findRoleByTitle("random_role");
        User user = new UserBuilder()
                .setUsername("user")
                .setRoles(Collections.singletonList(userRole))
                .setPassword("password")
                .build();
        userRepository.save(user);

        long bookId = bookRepository.findByTitleAuthorPublishedDate(book.getTitle(), book.getAuthor(), book.getPublishedDate()).get().getId();
        long userId = userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword()).getResult().getId();
        Order order = new Order(null, userId, bookId, LocalDateTime.now());
        assertTrue(orderRepository.save(order));
    }
}

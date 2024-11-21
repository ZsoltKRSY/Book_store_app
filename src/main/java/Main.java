import database.Bootstrap;
import database.DatabaseConnectionFactory;
import model.*;
import model.builder.*;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.security.RightsRolesRepository;
import repository.security.RightsRolesRepositoryMySQL;
import repository.user.UserRepository;
import repository.user.UserRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.user.AuthenticationService;
import service.user.AuthenticationServiceImpl;

import java.sql.Connection;
import java.time.LocalDate;

public class Main {

    public static void main(String[] args){

//        Book book = new BookBuilder()
//                .setTitle("Ion")
//                .setAuthor("Liviu Rebreanu")
//                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
//                .build();
//
//        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(false).getConnection();
//        BookRepository bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
//        bookRepository.removeAll();
//        BookService bookService = new BookServiceImpl(bookRepository);
//
//        bookService.save(book);
//        System.out.println(bookService.findAll());
//
//        bookService.save(new BookBuilder()
//                .setAuthor("Ioan Slavici")
//                .setTitle("Moara cu noroc")
//                .setPublishedDate(LocalDate.of(1950, 7, 3))
//                .build());
//        System.out.println(bookService.findAll());
//        System.out.println(bookService.findAll());
//
//        bookService.delete(book);
//        System.out.println(bookService.findAll());
//
//        bookService.save(book);
//        System.out.println(bookService.findAll());

        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(true).getConnection();

        BookRepository bookRepository = new BookRepositoryCacheDecorator(
                new BookRepositoryMySQL(connection),
                new Cache<>());
        BookService bookService = new BookServiceImpl(bookRepository);

        RightsRolesRepository rightsRolesRepository = new RightsRolesRepositoryMySQL(connection);
        UserRepository userRepository = new UserRepositoryMySQL(connection, rightsRolesRepository);

        AuthenticationService authenticationService = new AuthenticationServiceImpl(userRepository, rightsRolesRepository);

        if (userRepository.existsByUsername("qweqwe"))
            System.out.println("User already exists in database!");
        else
            authenticationService.register("qweqwe", "random12!");

        System.out.println(authenticationService.login("Zsolt", "parola123!"));
        System.out.println(authenticationService.login("asdasd", "random12!"));
    }
}

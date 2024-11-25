package launcher;

import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import model.Order;
import model.User;
import model.validator.Notification;
import repository.book.BookRepository;
import repository.book.BookRepositoryCacheDecorator;
import repository.book.BookRepositoryMySQL;
import repository.book.Cache;
import repository.order.OrderRepository;
import repository.order.OrderRepositoryMySQL;
import service.book.BookService;
import service.book.BookServiceImpl;
import service.order.OrderService;
import service.order.OrderServiceImpl;
import view.BookView;
import view.model.BookDTO;
import controller.BookController;

import java.sql.Connection;
import java.util.List;

public class BookStoreComponentFactory {
    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private final OrderRepository orderRepository;
    private final OrderService orderService;
    private static volatile BookStoreComponentFactory instance;

    public static BookStoreComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage, User loggedUser){
        if (instance == null){
            synchronized (BookStoreComponentFactory.class) {
                if (instance == null)
                    instance = new BookStoreComponentFactory(componentsForTest, primaryStage, loggedUser);
            }
        }

        return instance;
    }

    private BookStoreComponentFactory(Boolean componentsForTest, Stage primaryStage, User loggedUser){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryCacheDecorator(new BookRepositoryMySQL(connection), new Cache<>());
        this.bookService = new BookServiceImpl(bookRepository);

        this.orderRepository = new OrderRepositoryMySQL(connection);
        this.orderService = new OrderServiceImpl(orderRepository);

        List<BookDTO> BookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, BookDTOs);
        this.bookController = new BookController(bookView, bookService, orderService, loggedUser);
    }

    public BookView getBookView() {
        return bookView;
    }

    public BookController getBookController() {
        return bookController;
    }

    public BookRepository getBookRepository() {
        return bookRepository;
    }

    public BookService getBookService() {
        return bookService;
    }
}

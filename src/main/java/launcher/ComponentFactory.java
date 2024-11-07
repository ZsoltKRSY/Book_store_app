package launcher;

import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import model.Book;
import repository.BookRepository;
import repository.BookRepositoryMock;
import repository.BookRepositoryMySQL;
import service.BookService;
import service.BookServiceImpl;

import java.sql.Connection;
import java.util.List;

public class ComponentFactory {
    private final BookView bookView;
    private final BookController bookController;
    private final BookRepository bookRepository;
    private final BookService bookService;
    private static volatile ComponentFactory instance;

    public static ComponentFactory getInstance(Boolean componentsForTest, Stage primaryStage){
        if (instance == null){
            synchronized (ComponentFactory.class) {
                if (instance == null)
                    instance = new ComponentFactory(componentsForTest, primaryStage);
            }
        }

        return instance;
    }

    public ComponentFactory(Boolean componentsForTest, Stage primaryStage){
        Connection connection = DatabaseConnectionFactory.getConnectionWrapper(componentsForTest).getConnection();
        this.bookRepository = new BookRepositoryMySQL(connection);
        this.bookService = new BookServiceImpl(bookRepository);

        List<Book> books = bookService.findAll();
        this.bookView = new BookView(primaryStage, books);
        this.bookController = new BookController(bookView, bookService);
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

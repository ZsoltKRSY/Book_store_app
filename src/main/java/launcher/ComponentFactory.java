package launcher;

import database.DatabaseConnectionFactory;
import javafx.stage.Stage;
import mapper.BookMapper;
import repository.BookRepository;
import repository.BookRepositoryMySQL;
import service.BookService;
import service.BookServiceImpl;
import view.BookView;
import view.model.BookDTO;
import controller.BookController;

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

        List<BookDTO> BookDTOs = BookMapper.convertBookListToBookDTOList(bookService.findAll());
        this.bookView = new BookView(primaryStage, BookDTOs);
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

package controller;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import mapper.BookMapper;
import model.Book;
import model.Order;
import model.User;
import service.book.BookService;
import service.order.OrderService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;
    private final OrderService orderService;
    private final User loggedUser;


    public BookController(BookView bookView, BookService bookService, OrderService orderService, User loggedUser){
        this.bookView = bookView;
        this.bookService = bookService;
        this.orderService = orderService;
        this.loggedUser = loggedUser;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
        this.bookView.addSellButtonListener(new SellButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();
            LocalDate publishedDate = bookView.getPublishedDate();
            String priceString = bookView.getPrice();
            String stockString = bookView.getStock();

            if (title.isEmpty() || author.isEmpty() || priceString.isEmpty() || stockString.isEmpty())
                bookView.addDisplayAlertMessage("Save error", "Problem at input fields", "Can not have an empty Author, Title, Price or Stock field!");
            else{
                try {
                    long price = Long.parseLong(priceString);
                    int stock = Integer.parseInt(stockString);
                    if(price <= 0 || stock < 0)
                        throw new NumberFormatException();

                    BookDTO bookDTO = new BookDTOBuilder()
                            .setTitle(title)
                            .setAuthor(author)
                            .setPublishedDate(publishedDate)
                            .setPrice(price)
                            .setStock(stock)
                            .build();

                    Optional<Book> book = bookService.findByTitleAuthorPublishedDate(title, author, publishedDate);
                    if(book.isPresent()){
                        boolean updatedBook = bookService.updateStock(book.get(), book.get().getStock() + stock);
                        if(updatedBook){
                            BookDTO bookInObservableList = bookView.getBookFromObservableList(title, author, publishedDate);
                            bookInObservableList.setStock(bookInObservableList.getStock() + stock);
                            bookView.addDisplayAlertMessage("Save successful", "Stock increased", "Stock successfully increased for book!");
                        }
                        else
                            bookView.addDisplayAlertMessage("Save error", "Problem at adding book", "There was a problem at adding the book to the library!");
                    }
                    else{
                        boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));
                        if (savedBook) {
                            bookView.addBookToObservableList(bookDTO);
                            bookView.addDisplayAlertMessage("Save successful", "Book added", "Book was successfully added to the library!");
                        }
                        else
                            bookView.addDisplayAlertMessage("Save error", "Problem at adding book", "There was a problem at adding the book to the library!");
                    }
                }
                catch (NumberFormatException ex){
                    bookView.addDisplayAlertMessage("Save error", "Problem at Price or Stock fields", "The Price and Stock fields must be non-negative integer values!");
                }
            }
        }
    }

    private class DeleteButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if (bookDTO != null){
                boolean deletionSuccessful = bookService.delete(BookMapper.convertBookDTOToBook(bookDTO));

                if (deletionSuccessful){
                    bookView.removeBookFromObservableList(bookDTO);
                    bookView.addDisplayAlertMessage("Deletion successful", "Book deleted", "Book was successfully deleted from the library!");
                }
                else
                    bookView.addDisplayAlertMessage("Deletion error", "Problem at deleting book", "There was a problem deleting the selected book from the library!");
            }
            else
                bookView.addDisplayAlertMessage("Delete error", "Problem at deleting book", "You must select a book before pressing the Delete button!");
        }
    }

    private class SellButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            BookDTO bookDTO = bookView.getBookTableView().getSelectionModel().getSelectedItem();
            if(bookDTO != null){
                if (bookDTO.getStock() == 0)
                    bookView.addDisplayAlertMessage("Not in stock", "Problem at selling book", "The chosen book is currently not in stock!");
                else {
                    boolean sellSuccessful = bookService.updateStock(BookMapper.convertBookDTOToBook(bookDTO), bookDTO.getStock() - 1);

                    if (sellSuccessful) {
                        BookDTO bookInObservableList = bookView.getBookFromObservableList(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublishedDate());
                        bookInObservableList.setStock(bookInObservableList.getStock() - 1);
                        bookView.addDisplayAlertMessage("Successfully sold book", "Book sold", "Book was successfully sold from the library!");

                        Optional<Book> updatedBook = bookService.findByTitleAuthorPublishedDate(bookDTO.getTitle(), bookDTO.getAuthor(), bookDTO.getPublishedDate());
                        updatedBook.ifPresent(book -> orderService.save(new Order(null, loggedUser.getId(), book.getId(), LocalDateTime.now())));
                    }
                    else
                        bookView.addDisplayAlertMessage("Selling error", "Problem at selling book", "There was a problem selling the selected book from the library!");
                }
            }
            else
                bookView.addDisplayAlertMessage("Selling error", "Problem at selling book", "You must select a book before pressing the Sell button!");
        }
    }
}

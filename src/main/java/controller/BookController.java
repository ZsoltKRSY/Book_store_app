package controller;

import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import mapper.BookMapper;
import service.BookService;
import view.BookView;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

public class BookController {
    private final BookView bookView;
    private final BookService bookService;

    public BookController(BookView bookView, BookService bookService){
        this.bookView = bookView;
        this.bookService = bookService;

        this.bookView.addSaveButtonListener(new SaveButtonListener());
        this.bookView.addDeleteButtonListener(new DeleteButtonListener());
    }

    private class SaveButtonListener implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent actionEvent) {
            String title = bookView.getTitle();
            String author = bookView.getAuthor();

            if (title.isEmpty() || author.isEmpty())
                bookView.addDisplayAlertMessage("Save error", "Problem at Author or Title fields", "Can not have an empty Author or Title field!");
            else{
                BookDTO bookDTO = new BookDTOBuilder()
                        .setTitle(title)
                        .setAuthor(author)
                        .build();
                boolean savedBook = bookService.save(BookMapper.convertBookDTOToBook(bookDTO));

                if (savedBook) {
                    bookView.addDisplayAlertMessage("Save successful", "Book added", "Book was successfully added to the library!");
                    bookView.addBookToObservableList(bookDTO);
                }
                else
                    bookView.addDisplayAlertMessage("Save error", "Problem at adding book", "There was a problem at adding the book to the library!");
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
                    bookView.addDisplayAlertMessage("Deletion successful", "Book deleted", "Book was successfully deleted from the library!");
                    bookView.removeBookFromObservableList(bookDTO);
                }
                else
                    bookView.addDisplayAlertMessage("Deletion error", "Problem at deleting book", "There was a problem deleting the selected book from the library!");
            }
            else
                bookView.addDisplayAlertMessage("Delete error", "Problem at deleting book", "You must select a book before pressing the delete button!");
        }
    }
}

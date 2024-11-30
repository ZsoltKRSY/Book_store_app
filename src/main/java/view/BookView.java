package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import view.model.BookDTO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.time.LocalDate;
import java.util.List;

public class BookView {

    private TableView<BookDTO> bookTableView;
    private final ObservableList<BookDTO> booksObservableList;

    private TextField authorTextField;
    private TextField titleTextField;
    private TextField priceTextField;
    private TextField stockTextField;
    private DatePicker publishedDateDatePicker;
    private Label authorLabel;
    private Label titleLabel;
    private Label priceLabel;
    private Label stockLabel;
    private Label publishedDateLabel;
    private Button saveButton;
    private Button deleteButton;
    private Button sellButton;

    public BookView(Stage primaryStage, List<BookDTO> BookDTOs){
        primaryStage.setTitle("Bookstore");

        GridPane gridPane = new GridPane();
        initializeGridPane(gridPane);

        Scene scene = new Scene(gridPane, 720, 480);
        primaryStage.setScene(scene);

        booksObservableList = FXCollections.observableArrayList(BookDTOs);
        initTableView(gridPane);

        initSaveAndDelete(gridPane);

        primaryStage.show();
    }

    private void initializeGridPane(GridPane gridPane){
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
    }

    private void initTableView(GridPane gridPane){
        bookTableView = new TableView<>();

        bookTableView.setPlaceholder(new Label("No books to display."));

        TableColumn<BookDTO, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<BookDTO, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<BookDTO, LocalDate> publishedDateColumn = new TableColumn<>("Published Date");
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
        TableColumn<BookDTO, Long> priceColumn = new TableColumn<>("Price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        TableColumn<BookDTO, Integer> stockColumn = new TableColumn<>("Stock");
        stockColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));

        bookTableView.getColumns().addAll(titleColumn, authorColumn, publishedDateColumn, priceColumn, stockColumn);

        bookTableView.setItems(booksObservableList);

        gridPane.add(bookTableView, 0, 0, 5, 1);
    }

    private void initSaveAndDelete(GridPane gridPane){
        titleLabel = new Label("Title");
        gridPane.add(titleLabel, 1, 1);
        titleTextField = new TextField();
        gridPane.add(titleTextField, 2, 1);

        authorLabel = new Label("Author");
        gridPane.add(authorLabel, 3, 1);
        authorTextField = new TextField();
        gridPane.add(authorTextField, 4, 1);

        publishedDateLabel = new Label("Published Date");
        gridPane.add(publishedDateLabel, 1, 2);
        publishedDateDatePicker = new DatePicker(java.time.LocalDate.of(1990, 1, 1));
        gridPane.add(publishedDateDatePicker, 2, 2);

        priceLabel = new Label("Price");
        gridPane.add(priceLabel, 1, 3);
        priceTextField = new TextField();
        gridPane.add(priceTextField, 2, 3);

        stockLabel = new Label("Stock");
        gridPane.add(stockLabel, 3, 3);
        stockTextField = new TextField();
        gridPane.add(stockTextField, 4, 3);

        saveButton = new Button("Save");
        gridPane.add(saveButton, 5, 1);

        deleteButton = new Button("Delete");
        gridPane.add(deleteButton, 6, 1);

        sellButton = new Button("Sell");
        gridPane.add(sellButton, 7, 1);
    }

    public void addSaveButtonListener(EventHandler<ActionEvent> saveButtonListener){
        saveButton.setOnAction(saveButtonListener);
    }

    public void addDeleteButtonListener(EventHandler<ActionEvent> deleteButtonListener){
        deleteButton.setOnAction(deleteButtonListener);
    }

    public void addSellButtonListener(EventHandler<ActionEvent> sellButtonListener){
        sellButton.setOnAction(sellButtonListener);
    }

    public void addDisplayAlertMessage(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public String getTitle(){
        return titleTextField.getText();
    }

    public String getAuthor(){
        return authorTextField.getText();
    }

    public LocalDate getPublishedDate(){
        return publishedDateDatePicker.getValue();
    }

    public String getPrice(){
        return priceTextField.getText();
    }

    public String getStock(){
        return stockTextField.getText();
    }

    public BookDTO getBookFromObservableList(String title, String author, LocalDate publishedDate){
        return this.booksObservableList.stream()
                .filter(bDTO ->bDTO.getTitle().equals(title) && bDTO.getAuthor().equals(author) && bDTO.getPublishedDate().equals(publishedDate))
                .findFirst()
                .orElse(null);
    }

    public void addBookToObservableList(BookDTO bookDTO){
        this.booksObservableList.add(bookDTO);
    }

    public void removeBookFromObservableList(BookDTO bookDTO){
        this.booksObservableList.remove(bookDTO);
    }

    public TableView<BookDTO> getBookTableView(){
        return bookTableView;
    }
}

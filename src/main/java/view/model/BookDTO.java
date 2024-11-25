package view.model;

import javafx.beans.property.*;

import java.time.DateTimeException;
import java.time.LocalDate;

public class BookDTO {

    private StringProperty author;

    public StringProperty authorProperty(){
        if(author == null)
            author = new SimpleStringProperty(this, "author");

        return author;
    }

    public void setAuthor(String author){
        authorProperty().set(author);
    }

    public String getAuthor(){
        return authorProperty().get();
    }

    private StringProperty title;

    public StringProperty titleProperty() {
        if(title == null)
            title = new SimpleStringProperty(this, "title");

        return title;
    }

    public void setTitle(String title){
        titleProperty().set(title);
    }

    public String getTitle(){
        return titleProperty().get();
    }

    private ObjectProperty<LocalDate> publishedDate;

    public ObjectProperty<LocalDate> publishedDateProperty() {
        if(publishedDate == null)
            publishedDate = new SimpleObjectProperty<>(this, "publishedDate");

        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate){
        publishedDateProperty().set(publishedDate);
    }

    public LocalDate getPublishedDate(){
        return publishedDateProperty().get();
    }

    private LongProperty price;

    public LongProperty priceProperty(){
        if(price == null)
            price = new SimpleLongProperty(this, "price");

        return price;
    }

    public void setPrice(Long price){
        priceProperty().set(price);
    }

    public Long getPrice(){
        return priceProperty().get();
    }

    private IntegerProperty stock;

    public IntegerProperty stockProperty(){
        if(stock == null)
            stock = new SimpleIntegerProperty(this, "stock");

        return stock;
    }

    public void setStock(Integer stock){
        stockProperty().set(stock);
    }

    public Integer getStock(){
        return stockProperty().get();
    }
}

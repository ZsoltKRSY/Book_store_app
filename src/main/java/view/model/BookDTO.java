package view.model;

import javafx.beans.property.*;

public class BookDTO {
    private LongProperty id;

    public LongProperty idProperty(){
        if(id == null)
            id = new SimpleLongProperty(this, "id");

        return id;
    }

    public void setId(long id){
        idProperty().set(id);
    }

    public Long getId(){
        return idProperty().get();
    }

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

    public StringProperty titleProperty(){
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

    private LongProperty price;

    public LongProperty priceProperty(){
        if(price == null)
            price = new SimpleLongProperty(this, "price");

        return price;
    }

    public void setPrice(long price){
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

    public void setStock(int stock){
        stockProperty().set(stock);
    }

    public Integer getStock(){
        return stockProperty().get();
    }
}

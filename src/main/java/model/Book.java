package model;

import java.time.LocalDate;

public class Book {
    private Long id;
    private String title;
    private String author;
    private LocalDate publishedDate;
    private Long price;
    private Integer stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(LocalDate publishedDate) {
        this.publishedDate = publishedDate;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Book: Id=" + id + ", Title=" + title + ", Author=" + author + ", Published date=" + publishedDate + ", Price=" + price + ", Stock=" + stock;
    }

    @Override
    public boolean equals(Object object){
        if (object == null)
            return false;
        if (object.getClass() != this.getClass())
            return false;

        final Book book = (Book) object;
        return this.author.equals(book.getAuthor()) &&
                this.title.equals(book.getTitle()) &&
                this.publishedDate.equals(book.getPublishedDate());
    }
}

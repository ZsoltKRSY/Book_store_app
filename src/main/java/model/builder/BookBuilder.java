package model.builder;

import model.Book;

public class BookBuilder {
    private Book book;

    public BookBuilder(){
        book = new Book();
    }

    public BookBuilder setId(Long id){
        book.setId(id);
        return this;
    }
}

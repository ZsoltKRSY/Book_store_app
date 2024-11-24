package repository.book;

import model.Book;
import model.builder.BookBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookRepositoryMock implements BookRepository {
    private final List<Book> books;

    public BookRepositoryMock(){
        books = new ArrayList<>();
    }

    @Override
    public List<Book> findAll() {
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return books.parallelStream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        return books.parallelStream()
                .filter(item -> item.getTitle().equals(title) && item.getAuthor().equals(author))
                .findFirst();
    }

    @Override
    public boolean save(Book book) {
        return books.add(book);
    }

    @Override
    public boolean delete(Book book) {
        return books.remove(book);
    }

    @Override
    public boolean updateStock(Book book, int newStock){
        int i = books.indexOf(book);
        if(i < 0)
            return false;

        books.set(i, new BookBuilder()
                .setId(book.getId())
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .setPublishedDate(book.getPublishedDate())
                .setPrice(book.getPrice())
                .setStock(newStock)
                .build());
        return true;
    }

    @Override
    public void removeAll() {
        books.clear();
    }
}

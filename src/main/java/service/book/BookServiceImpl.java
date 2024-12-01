package service.book;

import model.Book;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    @Override
    public Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate) {
        return bookRepository.findByTitleAuthorPublishedDate(title, author, publishedDate);
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        return bookRepository.delete(book);
    }

    @Override
    public boolean updateStock(Book book, int newStock){
        return bookRepository.updateStock(book, newStock);
    }

    @Override
    public int getAgeOfBook(Long id) {
        Optional<Book> book = this.findById(id);
        LocalDate now = LocalDate.now();

        return book.map(value -> (int) ChronoUnit.YEARS.between(value.getPublishedDate(), now)).orElse(0);
    }
}

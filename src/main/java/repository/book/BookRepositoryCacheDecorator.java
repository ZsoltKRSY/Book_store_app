package repository.book;

import model.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator {
    private final Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache = cache;
    }

    @Override
    public List<Book> findAll() {
        if(cache.hasResult())
            return cache.load();

        List<Book> books = decoratedBookRepository.findAll();
        cache.save(books);

        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        if(cache.hasResult())
            return cache.load().stream()
                    .filter(item -> item.getId().equals(id))
                    .findFirst();

        return decoratedBookRepository.findById(id);
    }

    @Override
    public Optional<Book> findByTitleAuthorPublishedDate(String title, String author, LocalDate publishedDate) {
        if(cache.hasResult()){
            return cache.load().stream()
                    .filter(item -> item.getTitle().equals(title) && item.getAuthor().equals(author) && item.getPublishedDate().equals(publishedDate))
                    .findFirst();
        }

        return decoratedBookRepository.findByTitleAuthorPublishedDate(title, author, publishedDate);
    }


    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.save(book);
    }

    @Override
    public boolean delete(Book book) {
        cache.invalidateCache();
        return decoratedBookRepository.delete(book);
    }

    @Override
    public boolean updateStock(Book book, int newStock) {
        cache.invalidateCache();
        return decoratedBookRepository.updateStock(book, newStock);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedBookRepository.removeAll();
    }
}

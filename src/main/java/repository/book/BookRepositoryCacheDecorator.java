package repository.book;

import model.Book;

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
    public Optional<Book> findByTitleAndAuthor(String title, String author) {
        if(cache.hasResult()){
            return cache.load().stream()
                    .filter(item -> item.getTitle().equals(title) && item.getAuthor().equals(author))
                    .findFirst();
        }

        return decoratedBookRepository.findByTitleAndAuthor(title, author);
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
    public void removeAll() {
        cache.invalidateCache();
        decoratedBookRepository.removeAll();
    }
}

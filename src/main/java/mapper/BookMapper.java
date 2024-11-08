package mapper;

import model.Book;
import model.builder.BookBuilder;
import view.model.BookDTO;
import view.model.builder.BookDTOBuilder;

import java.time.LocalDate;
import java.util.List;

public class BookMapper {
    public static BookDTO convertBookToBookDTO(Book book){
        return new BookDTOBuilder()
                .setTitle(book.getTitle())
                .setAuthor(book.getAuthor())
                .build();
    }

    public static Book convertBookDTOToBook(BookDTO bookDTO){
        return new BookBuilder()
                .setTitle(bookDTO.getTitle())
                .setAuthor(bookDTO.getAuthor())
                .setPublishedDate(LocalDate.now())
                .build();
    }

    public static List<BookDTO> convertBookListToBookDTOList(List<Book> books){
        return books.parallelStream()
                .map()
    }
}

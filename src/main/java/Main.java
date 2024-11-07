import database.DatabaseConnectionFactory;
import model.*;
import model.builder.*;
import repository.*;

import java.time.LocalDate;

public class Main {

    public static void main(String[] args){
//        System.out.println("Hello world!\n");
//
//        Character.UnicodeBlock LocalDate;
//        Book book = new BookBuilder()
//                .setTitle("Ion")
//                .setAuthor("Liviu Rebreanu")
//                .setPublishedDate(java.time.LocalDate.of(1910, 10, 20))
//                .build();
//
//        System.out.println(book);
//
//        BookRepository bookRepository = new BookRepositoryMock();
//
//        bookRepository.save(book);
//        bookRepository.save(new BookBuilder()
//                .setAuthor("Ioan Slavici")
//                .setTitle("Moara cu noroc")
//                .setPublishedDate(java.time.LocalDate.of(1950, 7, 3))
//                .build());
//        System.out.println(bookRepository.findAll());
//        bookRepository.removeAll();
//        System.out.println(bookRepository.findAll());

        DatabaseConnectionFactory.getConnectionWrapper(false);
    }
}

package service.report;

import model.Book;
import model.User;

import java.util.List;
import java.util.Map;

public interface ReportService {

    Map<User, List<Map.Entry<Book, Integer>>> getBooksSoldByAllEmployees();

    Map<Book, Integer> getBooksSoldByEmployee(User user);
}

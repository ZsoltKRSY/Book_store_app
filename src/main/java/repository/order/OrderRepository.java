package repository.order;

import model.Book;
import model.Order;
import model.User;

import java.util.List;
import java.util.Map;

public interface OrderRepository {
    boolean save(Order order);

    Map<Book, Integer> getBooksSoldByEmployee(User user);

    void removeAll();
}

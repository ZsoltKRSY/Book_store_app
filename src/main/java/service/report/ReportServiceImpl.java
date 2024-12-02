package service.report;

import model.Book;
import model.User;
import repository.order.OrderRepository;
import repository.user.UserRepository;

import java.util.List;
import java.util.Map;

public class ReportServiceImpl implements ReportService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public ReportServiceImpl(UserRepository userRepository, OrderRepository orderRepository){
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public Map<User, List<Map.Entry<Book, Integer>>> getBooksSoldByAllEmployees() {
        List<User> users = userRepository.findAll();
        return null;
    }

    @Override
    public Map<Book, Integer> getBooksSoldByEmployee(User user) {
        return orderRepository.getBooksSoldByEmployee(user);
    }
}

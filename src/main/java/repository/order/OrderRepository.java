package repository.order;

import model.Order;

public interface OrderRepository {
    boolean save(Order order);

    void removeAll();
}

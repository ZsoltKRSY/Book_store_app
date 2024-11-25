package model;

import java.time.LocalDateTime;

public record Order(Long id, Long empl_id, Long book_id, LocalDateTime orderDate) {

    public Order(Long id, Long empl_id, Long book_id, LocalDateTime orderDate) {
        this.id = id;
        this.empl_id = empl_id;
        this.book_id = book_id;
        this.orderDate = orderDate;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public Long empl_id() {
        return empl_id;
    }

    @Override
    public Long book_id() {
        return book_id;
    }

    @Override
    public LocalDateTime orderDate() {
        return orderDate;
    }
}

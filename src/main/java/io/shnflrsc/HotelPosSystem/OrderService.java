package io.shnflrsc.HotelPosSystem;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderRepository.getAllOrders();
    }

    public List<OrderItem> getAllOrderItems() throws SQLException {
        return orderRepository.getAllOrderItems();
    }

    public int addDraftOrderAndReturnNewId(Order draftOrder) throws SQLException {
        return orderRepository.addDraftOrderAndReturnNewId(draftOrder);
    }

    public void cancelOrder(int orderId) throws SQLException {
        orderRepository.cancelOrder(orderId);
    }

    public void placeOrder(int orderId) throws SQLException {
        orderRepository.placeOrder(orderId);
    }

    public void addOrderItems(List<OrderItem> orderItems) throws SQLException {
        orderRepository.addOrderItems(orderItems);
    }

    public int addOrderItemAndReturnId(OrderItem orderItem) throws SQLException {
        return orderRepository.addOrderItemAndReturnId(orderItem);
    }

    public void editOrderTotal(int orderId, double newTotal) throws SQLException {
        orderRepository.editOrderTotal(orderId, newTotal);
    }

    public boolean orderItemExistsByMenuItemId(int menuItemId) throws SQLException {
        return orderRepository.orderItemExistsByMenuItemId(menuItemId);
    }

    public void logOrder(int orderId, LocalDateTime timestamp, double total) throws SQLException {
        orderRepository.logOrder(orderId, timestamp, total);
    }
}

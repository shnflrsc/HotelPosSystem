package io.shnflrsc.HotelPosSystem;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final Connection connection;

    public OrderRepository(Connection connection) {
        this.connection = connection;
    }

    public List<Order> getAllOrders() throws SQLException, IllegalArgumentException {
        List<Order> orders = new ArrayList<>();

        String sql = "SELECT * FROM orders";
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
                ) {
            while (rs.next()) {

                int id = rs.getInt("id");
                LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                double total = rs.getDouble("total");
                String paymentTypeStr = rs.getString("payment_type");
                PaymentType paymentType = PaymentType.valueOf(paymentTypeStr);
                String roomNumber = rs.getString("room_number");
                String statusStr = rs.getString("status");
                Status status = Status.valueOf(statusStr);

                orders.add(new Order (
                        id,
                        timestamp,
                        total,
                        paymentType,
                        roomNumber,
                        status
                ));
            }
        }
        return orders;
    }

    public int addDraftOrderAndReturnNewId(Order draftOrder) throws SQLException {
        String sql = "INSERT INTO orders (timestamp, total, payment_type, room_number, status) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = draftOrder.timestamp().format(formatter);
        ps.setString(1, formattedTimestamp);
        ps.setDouble(2, draftOrder.total());
        ps.setString(3, draftOrder.paymentType().toString());
        ps.setString(4, draftOrder.roomNumber());
        ps.setString(5, draftOrder.status().toString());

        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        } else {
            throw new SQLException("Failed to retrieve generated order ID.");
        }
    }

    public void cancelOrder(int orderId) throws SQLException {
        String sql = "UPDATE orders SET status = ?, timestamp = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, Status.CANCELLED.toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = LocalDateTime.now().format(formatter);
        ps.setString(2, formattedTimestamp);
        ps.setInt(3, orderId);
        ps.executeUpdate();
    }

    public void placeOrder(int orderId) throws SQLException {
        String sql = "UPDATE orders SET status = ?, timestamp = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setString(1, Status.PLACED.toString());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = LocalDateTime.now().format(formatter);
        ps.setString(2, formattedTimestamp);
        ps.setInt(3, orderId);
        ps.executeUpdate();
    }

    public void addOrderItems(List<OrderItem> orderItems) throws SQLException {
        String sql = "INSERT INTO order_items (order_id, menu_item_id, quantity) VALUES (?, ?, ?)";
        PreparedStatement ps = connection.prepareStatement(sql);

        for (OrderItem orderItem : orderItems) {
            ps.setInt(1, orderItem.orderId());
            ps.setInt(2, orderItem.menuItemId());
            ps.setInt(3, orderItem.quantity());
            ps.executeUpdate();
        }
    }

    public void editOrderTotal(int orderId, double newTotal) throws SQLException {
        String sql = "UPDATE orders SET total = ? WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setDouble(1, newTotal);
        ps.setInt(2, orderId);
        ps.executeUpdate();
    }
}

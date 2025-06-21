package io.shnflrsc.HotelPosSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
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

                orders.add(new Order (
                        id,
                        timestamp,
                        total,
                        paymentType,
                        roomNumber
                ));
            }
        }
        return orders;
    }
}

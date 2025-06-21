package io.shnflrsc.HotelPosSystem;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuItemRepository {
    private final Connection connection;

    public MenuItemRepository(Connection connection) {
        this.connection = connection;
    }

    public List<MenuItem> getAllMenuItems() throws SQLException {
        List<MenuItem> menuItems = new ArrayList<>();

        String sql = "SELECT * FROM menu_items";
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql)
                ) {
            while (rs.next()) {

                int id = rs.getInt("id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String category = rs.getString("category");


                menuItems.add(new MenuItem(
                        id,
                        name,
                        price,
                        category
                ));
            }
        }

        return menuItems;
    }
}

package io.shnflrsc.HotelPosSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public Optional<MenuItem> getMenuItemById(int queryId) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, queryId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            MenuItem menuItem = new MenuItem (rs.getInt("id"), rs.getString("name"), rs.getDouble("price"), rs.getString("category"));
            return Optional.of(menuItem);
        }
        return Optional.empty();
    }

    public boolean menuItemExists(int id) throws SQLException {
        String sql = "SELECT 1 FROM menu_items WHERE id = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        return rs.next();
    }
}

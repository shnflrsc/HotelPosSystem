package io.shnflrsc.HotelPosSystem;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() throws SQLException {
        return menuItemRepository.getAllMenuItems();
    }
    public boolean menuItemExists(int id) throws SQLException {
        return menuItemRepository.menuItemExists(id);
    }
    public Optional<MenuItem> getMenuItemById(int queryId) throws SQLException {
        return menuItemRepository.getMenuItemById(queryId);
    }
}

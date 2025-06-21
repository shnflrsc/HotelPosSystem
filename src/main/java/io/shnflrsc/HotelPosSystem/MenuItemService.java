package io.shnflrsc.HotelPosSystem;

import java.sql.SQLException;
import java.util.List;

public class MenuItemService {
    private final MenuItemRepository menuItemRepository;

    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> getAllMenuItems() throws SQLException {
        return menuItemRepository.getAllMenuItems();
    }
}

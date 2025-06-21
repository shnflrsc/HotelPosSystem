package io.shnflrsc.HotelPosSystem;

import java.sql.SQLException;

public class POSController {
    private POSView posView;
    private MenuItemService menuItemService;

    public POSController() {
        try {
            Database database = new Database();
            MenuItemRepository menuItemRepository = new MenuItemRepository(database.connect());
            this.posView = new POSView();
            this.menuItemService = new MenuItemService(menuItemRepository);
        } catch (SQLException e) {
            displayException(e);
        }
    }

    public void displayException(Exception e) {
        posView.displayException(e);
    }
    public void displayMenuItems() {
        try {
            posView.displayAllMenuItems(menuItemService.getAllMenuItems());
        } catch (SQLException e) {
            displayException(e);
        }
    }
}

package io.shnflrsc.HotelPosSystem;

import java.util.List;

public class POSView {
    public void displayAllMenuItems(List<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            System.out.println(menuItem.id() + " " + menuItem.name() + " ₱" + menuItem.price() + " " + menuItem.category());
        }
    }

    public void displayException(Exception e) {
        System.err.println(e);
    }
}
